package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.GeneralZonasDao
import com.mobile.massiveapp.domain.model.DoZonas
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class GetAllZonasUseCase @Inject constructor(
    private val zonasDao: GeneralZonasDao
) {
    suspend operator fun invoke():List<DoZonas> =
        zonasDao.getAll().map { it.toDomain() }
}