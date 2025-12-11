package com.mobile.massiveapp.domain.anexo

import com.mobile.massiveapp.data.database.dao.AnexoImagenDao
import com.mobile.massiveapp.domain.model.DoAnexoImagen
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllImagesByCodeUseCase @Inject constructor(
    private val anexoImagenDao: AnexoImagenDao
){
  fun getAllImagesByCode(code: String): Flow<List<DoAnexoImagen>> = anexoImagenDao.getAllByCode(code)

}