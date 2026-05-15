package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class GetLiquidacionPagoEdicionUseCase @Inject constructor(
    private val liquidacionPagoDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(accDocEntry:String) =
        try {
            liquidacionPagoDao.getLiquidacionPago(accDocEntry).toDomain()
        } catch (e:Exception){
            e.printStackTrace()
            null
        }
}