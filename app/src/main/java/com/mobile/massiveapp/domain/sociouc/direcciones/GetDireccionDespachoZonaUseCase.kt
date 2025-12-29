package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.model.SocioDirecciones
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import javax.inject.Inject

class GetDireccionDespachoZonaUseCase @Inject constructor(
    private val socioDireccionesDao: SocioDireccionesDao
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            socioDireccionesDao.getDireccionDespachoXZona(cardCode)
        } catch (e:Exception){
            e.printStackTrace()
            ""
        }
}