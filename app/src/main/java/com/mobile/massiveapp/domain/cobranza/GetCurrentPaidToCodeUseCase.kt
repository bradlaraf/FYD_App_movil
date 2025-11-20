package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.FacturasRepository
import javax.inject.Inject

class GetCurrentPaidToCodeUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val facturasRepository: FacturasRepository
) {
    suspend operator fun invoke(accDocEntry: String, docEntry: String):Double =
        try {
            val listaPagosDetalleEdicion = cobranzaRepository.getAllPagosDetallePorAccDocEntry(accDocEntry).filter { it.DocLine >= 1000 }
            listaPagosDetalleEdicion.filter { it.AccMigrated == "N" }

            val factura = facturasRepository.getFacturaPorDocEntry(docEntry)
            val test = cobranzaRepository.getAllPagosDetallePorDocEntry(docEntry).sumOf { it.SumApplied }
            val result = factura.PaidToDate - (listaPagosDetalleEdicion.sumOf { it.SumApplied } + test)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
}