package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountArticulosUseCase @Inject constructor(
    private val articuloDao: ArticuloDao
) {
    fun getCountArticulosFlow(): Flow<Int> =
        articuloDao.getAllCountArticulos()

}