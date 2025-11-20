package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.FacturasRepository
import javax.inject.Inject

class UpdateAllPagosDetalleUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val facturasRepository: FacturasRepository
) {
    suspend operator fun invoke(accDocEntry: String): Boolean =
        try {
            val pagosDetalleEditados = cobranzaRepository.getAllPagosDetallePorAccDocEntry(accDocEntry).filter { it.DocLine >= 1000 }

            if (pagosDetalleEditados.isNotEmpty()){
                cobranzaRepository.deleteAllPagosDetallesPorAccDocEntry(accDocEntry)
            }
            pagosDetalleEditados.forEach {
                it.DocLine = it.DocLine - 1000
                cobranzaRepository.saveCobranzaDetalle(it)
            }

                //Se eliminan las facturas duplicadas para la edición
            /*val cardCodeFactura = cobranzaRepository.getPagoCabeceraPorAccDocEntry(accDocEntry).CardCode
            val facturasToUpdate = facturasRepository.getAllFacturasPorCardCode(cardCodeFactura).filter { it.DocEntry >= 1000 }
            facturasToUpdate.forEach {
                it.Edit_Ptd = -11.0
                facturasRepository.updateFactura(it)
            }*/
            true
        }catch (e: Exception) {
            e.printStackTrace()
            false
        }
}