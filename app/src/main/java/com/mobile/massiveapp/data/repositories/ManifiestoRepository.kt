package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ManifiestoDao
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class ManifiestoRepository @Inject constructor(
    private val manifiestoDao: ManifiestoDao
) {
    suspend fun getAllManifiestos() =
        try {
            manifiestoDao.getAll().map { it.toDomain() }
        } catch (e: Exception){
            emptyList()
        }
}