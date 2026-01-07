package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class GetLiquidacionPorCodeYLineNumUseCase @Inject constructor(
    private val liquidacionPagoDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(accDocEntry: String, docLine: Int) =
        try {
            liquidacionPagoDao.getLiquidacionPago(accDocEntry = accDocEntry, docLine = docLine).toDomain()
        } catch (e:Exception){
            e.printStackTrace()
            null
        }
}