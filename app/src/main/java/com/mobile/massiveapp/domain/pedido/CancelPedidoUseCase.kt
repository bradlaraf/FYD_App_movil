package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import java.lang.Exception
import javax.inject.Inject

class CancelPedidoUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository,

    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao
) {
    var mensaje = "Pedido Cancelado"
    var codigo = 0
    suspend operator fun invoke(accDocEntry: String):DoError =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            val pedidoACancelar = pedidoRepository.getPedidoPorAccDocEntry(accDocEntry)
            pedidoACancelar.CANCELED = "Y"
            pedidoACancelar.AccFinalized = "N"
            pedidoRepository.savePedidoCabecera(pedidoACancelar)

            val estadoSesion = datosMaestrosService.getEstadoSesion(
                usuario,
                configuracion,
                url,
                10L
            )

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
                }
            }

            val hashPedidoACancelar = hashMapOf("ClientePedidos" to listOf(pedidoACancelar as Any))

            try {
                datosMaestrosRepository.sendUnDocumento(
                    hashPedidoACancelar,
                    configuracion,
                    usuario,
                    url
                )
            } catch (e:Exception){
                mensaje = e.message.toString()
            }

            DoError(
                mensaje,
                codigo
            )

        } catch (e: Exception){
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }
}
