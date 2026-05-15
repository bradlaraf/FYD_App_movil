package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.database.dao.ManifiestoDocumentoDao
import com.mobile.massiveapp.ui.view.util.SendData
import javax.inject.Inject

class GetAllPagosXManifiestoUseCase @Inject constructor(
    val liquidacionPagoDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(docEntry: Int) =
        try {
            liquidacionPagoDao.getAllPagosManifiesto(docEntry).mapIndexed { index, pago->
                pago.copy(DocLine = index)
            }
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}