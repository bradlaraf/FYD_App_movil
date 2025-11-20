package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.network.UsuariosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.UsuarioRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import javax.inject.Inject

class ResetearIDUnUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val errorLogDao: ErrorLogDao,
    private val usuariosService: UsuariosService,
    private val datosMaestrosService: DatosMaestrosService,

    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,
) {
    var mensaje = ""
    var errorCodigo = 0
    suspend operator fun invoke(userCode: String) =
        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)
            val timeOut = 30L

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
                        errorCodigo = ManagerInputData.SESION_CERRADA
                        throw Exception(mensaje)
                    }
                }
                is DoError ->{
                    mensaje = estadoSesion.ErrorMensaje
                    errorCodigo = estadoSesion.ErrorCodigo
                    errorLogDao.insert(getError(errorCodigo.toString(), mensaje))
                    throw Exception(mensaje)
                }
            }


            val usuarioParaEnviar = setUserToUpdate(usuarioRepository.getUsuarioToSend(userCode))

            val response = usuariosService.sendUsuario(
                listOf(usuarioParaEnviar),
                configuracion,
                usuario,
                url,
                timeOut
            )

            errorCodigo = 0
            val listaUsuario = response as? List<UsuarioToSend>?: emptyList()

            mensaje = when (response){
                is List<*> -> {
                    var mensajeIfAccError = "Reseteo de ID exitosa"

                    val accError =
                        try {
                            (response.first() as UsuarioToSend).AccError
                        } catch (e:Exception){
                            ""
                        }
                    if (accError.isNotEmpty()){
                        errorLogDao.insert(
                            getError(
                            code = "Insertar Usuario",
                            message = accError
                        )
                        )
                        mensajeIfAccError = "Envío fallido"
                    }
                    mensajeIfAccError
                }
                is DoError -> {
                    errorCodigo = response.ErrorCodigo
                    response.ErrorMensaje
                }
                else -> { "Sin conexión" }
            }

            if (listaUsuario.isNotEmpty()){
                val accError = listaUsuario.firstOrNull()?.AccError?:""
                usuarioRepository.insertUser(listaUsuario.first())
                if(accError.isNotEmpty()){
                    mensaje = accError
                }
            }

            if (errorCodigo != 0){
                errorLogDao.insert(
                    getError(
                    "$errorCodigo-$userCode",
                    mensaje
                )
                )
            }

            DoError(
                mensaje,
                errorCodigo
            )

        } catch (e:Exception){
            e.printStackTrace()
            DoError(e.message.toString(), errorCodigo)
        }

    private fun setUserToUpdate(usuarioParaEnviar: UsuarioToSend): UsuarioToSend {
        usuarioParaEnviar.AccAction = "I"
        usuarioParaEnviar.ResetIdPhone = "Y"
        usuarioParaEnviar.AccControl = "N"
        usuarioParaEnviar.AccStatusSession = "N"
        usuarioParaEnviar.ShowImage = "N"
        return  usuarioParaEnviar
    }

}