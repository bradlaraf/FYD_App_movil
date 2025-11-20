package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import javax.inject.Inject

class SearchArticuloUseCase @Inject constructor(
    val articuloDao: ArticuloDao
) {
    suspend operator fun invoke(text: String) =
        try {
            articuloDao.searchArticulos(text)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}