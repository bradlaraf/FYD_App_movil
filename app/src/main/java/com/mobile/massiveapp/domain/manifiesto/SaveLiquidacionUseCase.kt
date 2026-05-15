package com.mobile.massiveapp.domain.manifiesto

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.LiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import javax.inject.Inject

class SaveLiquidacionUseCase @Inject constructor (
    val liquidacionDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(liquidacion: DoLiquidacionPago) =
        try {
            val docLine = liquidacionDao.getLineNumeLiquidacion(liquidacion.U_MSV_MA_CLAVE)
            liquidacion.DocLine = docLine

            liquidacionDao.insert(liquidacion.toDatabase())
            true
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
}