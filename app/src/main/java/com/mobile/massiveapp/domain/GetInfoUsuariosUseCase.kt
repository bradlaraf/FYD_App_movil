package com.mobile.massiveapp.domain

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.model.DoError
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class GetInfoUsuariosUseCase @Inject constructor(
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao
) {
    var mensaje = "Usuarios Sincronizados"
    var codigo = 0
    suspend operator fun invoke(progressCallBack: (Int, String, Int) -> Unit): DoError =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            val estadoSesion = datosMaestrosService.getEstadoSesion(
                usuario,
                configuracion,
                url,
                10L
            )

            //Verificar Estado de la sesion
            when(estadoSesion){
                is String ->{
                    if (estadoSesion == "N"){
                        mensaje = "Su sesión esta cerrada"
                        codigo = ManagerInputData.SESION_CERRADA
                        throw Exception(mensaje)
                    }
                }
                is DoError ->{
                    mensaje = estadoSesion.ErrorMensaje
                    codigo = estadoSesion.ErrorCodigo
                    errorLogDao.insert(getError(codigo.toString(), mensaje))
                    throw Exception(mensaje)
                }
            }

            val dataUsuarios = listOf(
                "UsuariosAlmacenes",
                "UsuariosZonas",
                "UsuariosListaPrecios",
                "UsuariosGruposSocios",
                "UsuariosGrupoArticulos",
                "Usuarios",
                "Series",
                "CuentasC"
            )

            if (usuario.Code.isNotEmpty() && configuracion.IpPublica.isNotEmpty()) {

                datosMaestrosRepository.getDatosMaestrosFromEndpointAndSave(
                    dataUsuarios,
                    configuracion,
                    usuario,
                    url,
                    0
                ){ progress, message, maxLenght->
                    progressCallBack(progress, message, maxLenght)
                }

            }

            DoError(
                mensaje,
                codigo
            )
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            e.printStackTrace()

            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }
}