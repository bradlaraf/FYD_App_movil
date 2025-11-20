package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import javax.inject.Inject

class GetArticulosSinStockUseCase @Inject constructor(
    private val articulosDao: ArticuloDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke() =
        try {
            articulosDao.getAllArticulosInvSinStock()
        } catch (e:Exception){
            errorLogDao.insert(getError("${e.message}","${e.message}"))
            emptyList()
        }
}