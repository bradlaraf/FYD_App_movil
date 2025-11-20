package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
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

class CerrarTodasLasSesionesUseCase @Inject constructor(
    private val configurarUsuarioDao: ConfigurarUsuarioDao,
    private val cerrarSesionUsuarioUseCase: CerrarSesionUsuarioUseCase,

    private val usuarioRepository: UsuarioRepository,
    private val errorLogDao: ErrorLogDao,
    private val usuariosService: UsuariosService,
    private val datosMaestrosService: DatosMaestrosService,

    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,
) {
    var mensaje = ""
    var codigo = 0
    suspend operator fun invoke():DoError =
        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)
            /*
            val timeOut = 80L

            val usuariosParaEnviar = usuarioRepository.getAllUsuariosToSend().map { setUserToUpdate(it) }

            val response = usuariosService.sendUsuario(
                usuariosParaEnviar,
                configuracion,
                usuario,
                url,
                timeOut
            )

            var errorCodigo = 0
            val listaUsuario = response as? List<UsuarioToSend>?: emptyList()

            var mensaje = when (response){
                is List<*> -> {
                    var mensajeIfAccError = "Sesiones cerradas exitosamente"

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
                        mensajeIfAccError = "Operación fallida"
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
                usuarioRepository.inserAlltUsers(listaUsuario)
                if(accError.isNotEmpty()){
                    mensaje = accError
                }
            }

            if (errorCodigo != 0){
                errorLogDao.insert(
                    getError(
                    "$errorCodigo",
                    mensaje
                )
                )
            }

            DoError(
                mensaje,
                errorCodigo
            )*/

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


            val listaCodesUsuario = configurarUsuarioDao.getAll().map { it.Code }
            val listaErrores = listaCodesUsuario.map { code->
                cerrarSesionUsuarioUseCase(code)
            }
            if (listaErrores.all { it.ErrorCodigo == 0 }){
                mensaje = "Cierre sesiones exitoso"
                codigo = 0
            } else {
                mensaje = listaErrores.firstOrNull()?.ErrorMensaje?:""
                codigo = listaErrores.firstOrNull()?.ErrorCodigo?:-1
            }

            DoError(
                ErrorMensaje = mensaje,
                ErrorCodigo =  codigo
            )
        } catch (e:Exception){
            e.printStackTrace()
            DoError(e.message.toString(), -1)
        }

}