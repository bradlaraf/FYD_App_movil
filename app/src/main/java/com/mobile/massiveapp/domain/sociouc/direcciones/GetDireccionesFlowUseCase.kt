package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.MassiveApp.Companion.prefsSocio
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.SocioDireccionesEntity
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.mobile.massiveapp.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDireccionesFlowUseCase @Inject constructor(
    private val socioDireccionesDao: SocioDireccionesDao
) {
    fun getDireccionesFlow(): Flow<List<DoSocioDirecciones>> = socioDireccionesDao.getDireccionesFlow(prefsSocio.getCardCode()).map { listaDirecciones->
        listaDirecciones.map { it.toDomain() }
    }
}