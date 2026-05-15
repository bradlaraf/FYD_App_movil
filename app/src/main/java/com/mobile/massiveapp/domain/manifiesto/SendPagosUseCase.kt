package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.model.LiquidacionPago
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.ui.view.util.SendData
import javax.inject.Inject

class SendPagosUseCase @Inject constructor(
    private val liquidacionPagoDao: LiquidacionPagoDao,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,

    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao
) {
    var mensaje = "Pagos enviados"
    var codigo = 0
    suspend operator fun invoke() =
        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)

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

            val listaPagos = liquidacionPagoDao.getAllPagos(SendData.instance.docEntryFactura)

            listaPagos.forEach { pago->

                val sendPago = datosMaestrosService.sendPagoLiquidacion(
                    pago.toModel(),
                    configuracion,
                    usuario,
                    url,
                    11L
                )


                mensaje = when (sendPago){
                    is List<*> -> {
                        var mensajeIfAccError = "Registro de pago exitoso"

                        val accError =
                            try {
                                (sendPago.first() as LiquidacionPago).AccError
                            } catch (e:Exception){
                                ""
                            }
                        if (accError.isNotEmpty()){
                            errorLogDao.insert(getError(
                                code = "Enviar Pago",
                                message = accError
                            ))
                            mensajeIfAccError = "Envío fallido"
                        }
                        mensajeIfAccError
                    }

                    is DoError -> {
                        codigo = sendPago.ErrorCodigo
                        sendPago.ErrorMensaje
                    }
                    else -> { "Sin conexión" }
                }
            }

            datosMaestrosRepository.getDatosMaestrosFromEndpointAndSave(listOf(
                "ClienteLiquidacionPagos",
                "Manifiestos"),
                configuracion,
                usuario,
                url,
                600,
                5L
            ){progress, message, maxLenght->}

            DoError(
                mensaje,
                codigo
            )
        } catch (e:Exception){
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }

}