package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.ManifiestoDao
import com.mobile.massiveapp.domain.model.DoManifiestoView
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetManifiestosPendientesUseCase @Inject constructor(
    private val manifiestoDao: ManifiestoDao
) {
    fun getManifiestosPendientes(fechaInicio: String, fechaFin: String): Flow<List<DoManifiestoView>> =
        manifiestoDao.getManifiestosPendientes(fechaInicio, fechaFin)
}
