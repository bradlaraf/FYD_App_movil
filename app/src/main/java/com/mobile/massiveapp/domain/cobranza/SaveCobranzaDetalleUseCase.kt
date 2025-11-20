package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import javax.inject.Inject

class SaveCobranzaDetalleUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val clientePagosDetalleDao: ClientePagosDetalleDao
) {
    /** Solo puede haber un detalle de pago por factura **/
    suspend operator fun invoke(cobranzaDetalle: DoClientePagoDetalle):Boolean =
        try {
            val pagosDetalleRegistrados = clientePagosDetalleDao.getPagoDetalle(cobranzaDetalle.AccDocEntry, cobranzaDetalle.DocEntry)

            if (pagosDetalleRegistrados.isNotEmpty()){
                val docLineActual = pagosDetalleRegistrados.first().DocLine
                cobranzaDetalle.DocLine = docLineActual
                clientePagosDetalleDao.update(cobranzaDetalle.toDatabase())
            } else {
                cobranzaRepository.saveCobranzaDetalle(cobranzaDetalle)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}