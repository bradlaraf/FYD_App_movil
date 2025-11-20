package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.GeneralCondicionesDao
import com.mobile.massiveapp.domain.getError
import javax.inject.Inject

class GetCondicionesSocioUseCase @Inject constructor(
    private val generalCondicionesDao: GeneralCondicionesDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            generalCondicionesDao.getCondicionesDisponibles(cardCode)
        } catch (e: Exception){
            errorLogDao.insert(getError("${e.message}","${e.message}"))
            emptyList()
        }
}