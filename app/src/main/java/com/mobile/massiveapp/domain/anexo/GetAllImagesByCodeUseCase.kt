package com.mobile.massiveapp.domain.anexo

import com.mobile.massiveapp.data.database.dao.AnexoImagenDao
import com.mobile.massiveapp.data.database.entities.AnexoImagenEntity
import com.mobile.massiveapp.domain.model.DoAnexoImagen
import com.mobile.massiveapp.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllImagesByCode @Inject constructor(
    private val anexoImagenDao: AnexoImagenDao
){
  fun getAllImagesByCode(code: String): Flow<List<DoAnexoImagen>> = anexoImagenDao.getAllByCode(code).map { it.map { it.toDomain() } }

}