package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.BasesDao
import javax.inject.Inject

class GetBaseActualUseCase @Inject constructor(
    private val basesDao: BasesDao
) {
    suspend operator fun invoke() =
        try {
            basesDao.getBaseActual().CompnyName
        } catch (e:Exception){
            e.printStackTrace()
            ""
        }
}