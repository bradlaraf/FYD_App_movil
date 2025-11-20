package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.ui.view.util.agregarPagoDetalle
import javax.inject.Inject

class SavePedidosDetalleParaCobranzaDirectaUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val facturasRepository: FacturasRepository,
    private val loginRepository: LoginRepository,
    private val facturasDao: ClienteFacturasDao
) {
    suspend operator fun invoke(docEntry: String, accDocEntry: String):Boolean =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val factura = facturasRepository.getFacturaPorDocEntry(docEntry)

            val detalleAGuardar = agregarPagoDetalle(
                monto = factura.PaidToDate,
                accDocEntry = accDocEntry,
                docLine = 0,
                docEntryFactura = factura.DocEntry,
                instId = factura.InstlmntID,
                usuario = usuario.Code
            )

            /*facturasDao.restarPaidToDate(factura.DocEntry, factura.DocTotal)*/
            cobranzaRepository.saveCobranzaDetalle(detalleAGuardar)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}