package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import javax.inject.Inject

class GetAllDireccionesViewUseCase @Inject constructor(
    private val direccionesDao: SocioDireccionesDao
) {
    suspend operator fun invoke(carcCode: String) =
        try {
            direccionesDao.getDireccionesView(cardCode = carcCode)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}