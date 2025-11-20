package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.CuentasCDao
import com.mobile.massiveapp.data.database.entities.toDomain
import javax.inject.Inject

class GetAllCuentasCUseCase @Inject constructor(
    private val cuentasCDao: CuentasCDao
) {
    suspend operator fun invoke() =
        try {
            cuentasCDao.getAll().map { it.toDomain() }
        }catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}