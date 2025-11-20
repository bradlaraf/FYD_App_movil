package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.domain.model.DoArticulo
import com.mobile.massiveapp.domain.model.toDomain
import java.lang.Exception
import javax.inject.Inject

class GetAllArticulosFromDatabaseUseCase @Inject constructor(
    private val articuloDao: ArticuloDao
){
    suspend operator fun invoke(): List<DoArticulo> =
        try {
            articuloDao.getAllArticulosWithLimit().map { it.toDomain() }
        } catch (ex: Exception){
            ex.printStackTrace()
            emptyList()
        }
}

