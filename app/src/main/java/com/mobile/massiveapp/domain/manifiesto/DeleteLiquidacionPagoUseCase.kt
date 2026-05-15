package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.ui.view.util.SendData
import javax.inject.Inject

class DeleteLiquidacionPagoUseCase @Inject constructor(
    private val liquidacionDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(accDocEntry: String) =
        try {
            val liquidacionACancelar = liquidacionDao.getLiquidacionPago(accDocEntry = accDocEntry)
            if (liquidacionACancelar.AccMigrated == "N"){
                liquidacionDao.deletePagoLiquidacion(liquidacionACancelar.AccDocEntry)
                /*val pagosDetalleToUpdateDocLineList = liquidacionDao.getAllLiquidacionesPorAccDocEntry(SendData.instance.docEntryFactura).filter { it.DocLine > docLine }

                if (pagosDetalleToUpdateDocLineList.isNotEmpty()){
                    pagosDetalleToUpdateDocLineList.forEach {

                        liquidacionDao.updateDocLine(
                            updateDocLine = it.DocLine -1,
                            accDocEntry = it.AccDocEntry
                        )
                    }
                }*/
            } else {
                liquidacionDao.deletePago(liquidacionACancelar.AccDocEntry)
            }


            true
        } catch (e:Exception){
            e.printStackTrace()
            false
        }
}