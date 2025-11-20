package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.FacturasRepository
import javax.inject.Inject

class DeleteAllPagoDetalleParaEditarUseCase @Inject constructor(
    private val repository: CobranzaRepository,
    private val facturasRepository: FacturasRepository
){
    suspend operator fun invoke(accDocEntry: String):Boolean =
        try {

            val pagoDetalles = repository.getAllPagosDetallePorAccDocEntry(accDocEntry).filter { it.DocLine >= 1000 }
            pagoDetalles.forEach { pagoDetalle ->
                repository.deleteUnPagoDetallePorAccDocEntryYDocLine(accDocEntry, pagoDetalle.DocLine)

                    //Eliminar las facturas duplicadas para edicion
                val cardCodePago = repository.getPagoCabeceraPorAccDocEntry(pagoDetalle.AccDocEntry).CardCode
                val facturasPago = facturasRepository.getAllFacturasPorCardCode(cardCodePago)
                facturasPago.forEach {
                    it.Edit_Ptd = -11.0
                    facturasRepository.updateFactura(it)
                }
            }
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}