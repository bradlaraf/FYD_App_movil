package com.mobile.massiveapp.domain.pedido

import android.content.Context
import android.widget.Toast
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.network.UsuariosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.UsuarioRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.isInternetAvailable
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class ComprobarEstadoActualPedidoUseCase @Inject constructor(
    private val errorLogDao: ErrorLogDao,
    private val datosMaestrosService: DatosMaestrosService,
    private val pedidosDao: ClientePedidosDao,

    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,

    private val datosMaestrosRepository: DatosMaestrosRepository
) {

    var mensaje = "Puede editar el pedido"
    var errorCodigo = 0
    suspend operator fun invoke(accDocEntry: String, context: Context) =
        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)
            val timeOut = 30L

            val estadoSesion = datosMaestrosService.getEstadoSesionWithConectivityCheck(
                usuario,
                configuracion,
                url,
                10L
            )

            //Verificar conexion a internet
            if (!context.isInternetAvailable()){
                mensaje = "No tiene conexión a internet"
                errorCodigo = -1
                throw Exception(mensaje)
            }


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

            val sincComplete = getAllDocumentos(configuracion, usuario, url)

            val pedidoSincronizado =
                if (sincComplete){
                    pedidosDao.getPedidoPorAccDocEntry(accDocEntry)
                } else {
                    null
                }


            if (
                pedidoSincronizado != null
                && pedidoSincronizado.DocEntry != -1
                && (pedidoSincronizado.NumAtCard.trim().isNotEmpty()
                || pedidoSincronizado.ObjType == 17) ){
                errorCodigo = -2
                mensaje = "No se puede editar el pedido"
            }

            DoError(
                mensaje,
                errorCodigo
            )

        } catch (e:Exception){
            e.printStackTrace()
            DoError(e.message.toString(), errorCodigo)
        }

    private suspend fun getAllDocumentos(configuracion: DoConfiguracion, usuario:DoUsuario, url:String): Boolean {
        return datosMaestrosRepository.getDatosMaestrosFromEndpointAndSave(listOf(
            "ClientePagos",
            "ClientePedidos"),
            configuracion,
            usuario,
            url
        ){ progress, message, maxLenght-> }
    }
}