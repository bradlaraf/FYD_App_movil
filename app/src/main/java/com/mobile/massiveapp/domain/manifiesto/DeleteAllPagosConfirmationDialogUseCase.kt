package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import javax.inject.Inject

class DeleteAllPagosConfirmationDialogUseCase @Inject constructor(
    private val liquidacionPagoDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(accDocEntry:String) =
        try {
            liquidacionPagoDao.deletePagos()
            true
        } catch (e:Exception){
            false
        }
}