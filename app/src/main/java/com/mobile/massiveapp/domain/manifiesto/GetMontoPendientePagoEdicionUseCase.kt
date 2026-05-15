package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.database.dao.ManifiestoDocumentoDao
import javax.inject.Inject

class GetMontoPendientePagoEdicionUseCase @Inject constructor(
    private val liquidacionPagoDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(accDocEntry:String) =
        try {
            liquidacionPagoDao.getMontoPendientePago(accDocEntry)
        } catch (e:Exception){
            e.printStackTrace()
            0.0
        }
}