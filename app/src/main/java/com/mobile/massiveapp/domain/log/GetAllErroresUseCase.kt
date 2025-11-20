package com.mobile.massiveapp.domain.log

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import javax.inject.Inject

class GetAllErroresUseCase @Inject constructor(
    val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke() =
        errorLogDao.getAll()
}