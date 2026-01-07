package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.ui.view.util.agregarPagoDetalle
import com.mobile.massiveapp.ui.view.util.agregarPagoLiquidacion
import javax.inject.Inject

class SavePedidosDetalleParaCobranzaDirectaUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val facturasRepository: FacturasRepository,
    private val loginRepository: LoginRepository,
    private val facturasDao: ClienteFacturasDao,
    private val liquidacionPagoDao: LiquidacionPagoDao
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
            val liquidacionAGuardar = agregarPagoLiquidacion(
                docLine = liquidacionPagoDao.getLineNumeLiquidacion(accDocEntry),
                monto = factura.PaidToDate,
                accDocEntry = accDocEntry,
                numeroOperacion = "",
                numeroCuenta = usuario.DefaultAcctCodeEf,
                moneda = usuario.DefaultCurrency,
                medio = "E",
                instId = factura.InstlmntID,
                liquidacion = -1,
                manifiesto = -1,
                docEntryFactura = factura.DocEntry,
            )

            /*facturasDao.restarPaidToDate(factura.DocEntry, factura.DocTotal)*/
           // cobranzaRepository.saveCobranzaDetalle(detalleAGuardar)
            liquidacionPagoDao.insert(liquidacionAGuardar.toDatabase())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}