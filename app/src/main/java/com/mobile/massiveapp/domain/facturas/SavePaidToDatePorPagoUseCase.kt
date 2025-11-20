package com.mobile.massiveapp.domain.facturas

import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.ui.view.util.format
import javax.inject.Inject

class SavePaidToDatePorPagoUseCase @Inject constructor(
    private val facturasRepository: FacturasRepository
) {

    /***** Metodo obsoleto para la logica de registrar detalle de pagos segun el app de Seidor *****/
    suspend operator fun invoke(docEntry: String, montoARestar: Double):Boolean =
        try {
            val currentEditPtd = facturasRepository.getFacturaPorDocEntry(docEntry).Edit_Ptd
            val resultadoAIngresar = currentEditPtd - montoARestar

            //@Query("UPDATE Factura SET Edit_Ptd = :editPtd WHERE DocEntry = :docEntry")
            facturasRepository.updateEditPtd(docEntry, resultadoAIngresar.format(2))
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

}