package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import javax.inject.Inject

class GetArticulosConStockUseCase @Inject constructor(
    private val articulosDao: ArticuloDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            if (cardCode.isEmpty()) articulosDao.getAllArticulosInvConStocks()
            else articulosDao.getAllArticulosInvConStocks(cardCode)

        } catch (e:Exception){
            errorLogDao.insert(getError("${e.message}","${e.message}"))
            emptyList()
        }
}