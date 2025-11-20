package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.domain.model.DoArticuloInventario
import java.lang.Exception
import javax.inject.Inject

class GetAllArticulosUseCase @Inject constructor(
    private val articuloDao: ArticuloDao
) {
    suspend operator fun invoke(): List<DoArticuloInventario> =
        try {
            articuloDao.getAllArticulosInventario()
        } catch (ex: Exception){
            ex.printStackTrace()
            emptyList()
        }
}