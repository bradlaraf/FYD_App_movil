package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import javax.inject.Inject

class EditarLiquidacionPagoUseCase @Inject constructor(
    private val liquidacionPagoDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(liquidacion: DoLiquidacionPago) =
        try {
            liquidacionPagoDao.insert(liquidacion.toDatabase())
             true
        } catch (e:Exception){
            e.printStackTrace()
            false
        }
}