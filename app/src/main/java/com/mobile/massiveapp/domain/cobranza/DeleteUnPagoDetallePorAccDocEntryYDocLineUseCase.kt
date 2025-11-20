package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.ui.view.util.format
import javax.inject.Inject

class DeleteUnPagoDetallePorAccDocEntryYDocLineUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val facturasRepository: FacturasRepository
) {

    /**  **/
    suspend operator fun invoke(accDocEntry: String, docLine: Int): Boolean =
        try {

                //Se retorna el PaidToDate a la factura
            val currentDetalle = cobranzaRepository.getCobranzaDetallePorAccDocEntryYLineNum(accDocEntry, docLine.toString())
            val facturaActual = facturasRepository.getFacturaPorDocEntry(currentDetalle.DocEntry.toString())
            val montoAReponer = facturaActual.Edit_Ptd + currentDetalle.SumApplied
            val editPtdActualizado = facturasRepository.sumarEditPtdPorPagoEliminado(montoAReponer.format(2) , currentDetalle.DocEntry)


                //Se elimina el pago detalle
            var deleteSuccess = false
            if (editPtdActualizado){
                deleteSuccess = cobranzaRepository.deleteUnPagoDetallePorAccDocEntryYDocLine(accDocEntry, docLine)
            }

                //Se actualizan los docLine de los pagos detalle
            if (deleteSuccess){
                val pagosDetalleToUpdateDocLineList = cobranzaRepository.getAllPagosDetallePorAccDocEntry(accDocEntry).filter { it.DocLine > docLine }
                if (pagosDetalleToUpdateDocLineList.isNotEmpty()){
                    pagosDetalleToUpdateDocLineList.forEach {
                        cobranzaRepository.deleteUnPagoDetallePorAccDocEntryYDocLine(it.AccDocEntry, it.DocLine)
                        it.DocLine -= 1
                        cobranzaRepository.saveCobranzaDetalle(it)
                    }
                }
            }

            true
        } catch (e: Exception) {
            false
        }
}