package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.SeriesNDao
import com.mobile.massiveapp.data.database.entities.toDomain
import javax.inject.Inject

class GetAllGeneralSeriesUseCase @Inject constructor(
    private val seriesDao: SeriesNDao
) {
    suspend operator fun invoke() =
        try {
            seriesDao.getAll().map { it.toDomain() }
        }catch (e:Exception){
            emptyList()
        }
}