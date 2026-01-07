package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import javax.inject.Inject

class GetArticuloInfoBaseViewUseCase @Inject constructor(
    private val articulosDao: ArticuloDao
) {
    suspend operator fun invoke(itemCode: String) =
        try {
            articulosDao.getArticuloInfoBaseView(itemCode)
        } catch (e:Exception){
            e.printStackTrace()
            null
        }
}