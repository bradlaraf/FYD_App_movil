package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDao
import com.mobile.massiveapp.data.database.entities.RutaComercialEntity
import com.mobile.massiveapp.domain.model.DoRutaComercial
import com.mobile.massiveapp.domain.model.DoRutaComercialView
import com.mobile.massiveapp.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllRutasComercialUseCase @Inject constructor(
    private val rutaComercialDao: RutaComercialDao
) {
    fun getAllRutas(fechaInicio: String, fechaFin: String): Flow<List<DoRutaComercialView>> =
        rutaComercialDao.getAllFlow(fechaInicio = fechaInicio, fechaFin = fechaFin)
}
