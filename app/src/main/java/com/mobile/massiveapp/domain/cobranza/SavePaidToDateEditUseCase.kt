package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.ui.view.util.format
import javax.inject.Inject

class SavePaidToDateEditUseCase @Inject constructor(
    private val facturasRepository: FacturasRepository
){
    suspend operator fun invoke(docEntry: String, paidToDate: Double):Boolean =
        try {
            val currentPaidToDate = facturasRepository.getFacturaPorDocEntry(docEntry).Edit_Ptd
            val resultadoAIngresar = currentPaidToDate - paidToDate

            //Guarda el valor en el campo EditPtd
            facturasRepository.updateEditPtd(docEntry, resultadoAIngresar.format(2))
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}