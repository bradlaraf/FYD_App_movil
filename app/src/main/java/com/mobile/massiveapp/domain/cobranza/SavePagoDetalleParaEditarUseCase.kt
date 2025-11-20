package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import javax.inject.Inject

class SavePagoDetalleParaEditarUseCase @Inject constructor(
    private val pagosDetalleDao: ClientePagosDetalleDao
) {
    /** Solo puede haber un detalle de pago por factura **/
    suspend operator fun invoke(clientePagoDetalle: DoClientePagoDetalle): Boolean =
        try {
            val pagoDetalleBd = pagosDetalleDao.getPagoDetalle(clientePagoDetalle.AccDocEntry, clientePagoDetalle.DocEntry)

            val listaFiltrada = pagoDetalleBd.filter { it.DocLine >= 1000 }

            if (listaFiltrada.isNotEmpty()){
                val listaEditables = pagoDetalleBd.filter { it.DocLine >=1000 }
                val docLineActual = listaEditables.firstOrNull()?.DocLine?:-22
                clientePagoDetalle.DocLine = docLineActual
                pagosDetalleDao.update(clientePagoDetalle.toDatabase())
            } else {
                pagosDetalleDao.saveUnaCobranzaDetalle(clientePagoDetalle.toDatabase())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}