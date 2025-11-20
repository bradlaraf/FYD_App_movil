package com.mobile.massiveapp.domain.facturas

import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.domain.model.DoClienteFacturas
import javax.inject.Inject

class SetEditPtdLikePaidToDateUseCase @Inject constructor(
    private val facturasRepository: FacturasRepository
) {

    /***** Metodo obsoleto para la logica de registrar detalle de pagos segun el app de Seidor *****/
    suspend operator fun invoke(docEntry: Int) =
        try {
            //@Query("UPDATE Factura SET Edit_Ptd = PaidToDate WHERE DocEntry = :docEntry AND Edit_Ptd == -11.0")
            facturasRepository.setEditPtdEqualPaidToCode(docEntry)
            //DoClienteFacturas()
            facturasRepository.getFacturaPorDocEntry(docEntry.toString())
        } catch (e: Exception){
            e.printStackTrace()
            DoClienteFacturas()
        }

}