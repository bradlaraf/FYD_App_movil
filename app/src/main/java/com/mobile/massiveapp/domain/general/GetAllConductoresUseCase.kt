package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.ConductorDao
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class GetAllConductoresUseCase @Inject constructor(
    private val conductorDao: ConductorDao
) {
    suspend operator fun invoke()=
        try {
            conductorDao.getAllOrder().map { it.toDomain() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}