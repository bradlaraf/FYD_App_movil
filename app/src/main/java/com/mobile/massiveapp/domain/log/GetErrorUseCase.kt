package com.mobile.massiveapp.domain.log

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import javax.inject.Inject

class GetErrorUseCase @Inject constructor(
    val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke(date: String, hour: String) =
        errorLogDao.getError(date, hour)
}