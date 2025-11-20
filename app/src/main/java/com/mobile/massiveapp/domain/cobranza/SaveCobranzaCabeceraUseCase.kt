package com.mobile.massiveapp.domain.cobranza

import android.content.Context
import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.model.ClientePagos
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import javax.inject.Inject

class SaveCobranzaCabeceraUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val facturasDao: ClienteFacturasDao,

    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao
) {
    var mensaje = "Cobranza enviada"
    var codigo = 0
    suspend operator fun invoke(cobranzaCabecera: ClientePagos, context: Context):DoError =
        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)


            cobranzaRepository.saveCobranzaCabecera(cobranzaCabecera)

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

                //Update al PaidToDate de las Facturas en el Pago
            val listaPagosDetalle = cobranzaRepository.getAllPagosDetallePorAccDocEntry(cobranzaCabecera.AccDocEntry)

            listaPagosDetalle.forEach {
                facturasDao.restarPaidToDate(
                    docEntry = it.DocEntry,
                    paidToDate =  it.SumApplied
                )
            }

            val hashMapToSend = hashMapOf("ClientePagos" to listOf(cobranzaCabecera as Any))

                //Se envia la cobranza
            val sendSuccess = datosMaestrosRepository.sendUnDocumento(
                hashMapToSend,
                configuracion,
                usuario,
                url,
                11L
            )

            if (sendSuccess){
                //Se sincronizan todos los pagos
                cobranzaRepository.saveCobranzaCabecera(cobranzaCabecera)
                datosMaestrosRepository.getDatosMaestrosFromEndpointAndSave(listOf(
                    "ClientePagos"),
                    configuracion,
                    usuario,
                    url,
                    600,
                    3L
                ){progress, message, maxLenght->}
            } else {
                cobranzaRepository.saveCobranzaCabecera(cobranzaCabecera)
            }

            DoError(
                mensaje,
                codigo
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }
}