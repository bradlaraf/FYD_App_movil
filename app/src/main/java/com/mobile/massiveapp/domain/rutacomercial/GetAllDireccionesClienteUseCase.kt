package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.domain.model.DoDireccion
import javax.inject.Inject

class GetAllDireccionesClienteUseCase @Inject constructor(
    private val socioDireccionesDao: SocioDireccionesDao
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            socioDireccionesDao.getDireccionesCliente(cardCode)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList<DoDireccion>()
        }
}
