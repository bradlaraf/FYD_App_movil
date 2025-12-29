package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.repositories.SocioDireccionesRepository
import com.mobile.massiveapp.domain.model.DoDireccionEdicionView
import com.mobile.massiveapp.domain.model.DoDireccionView
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import timber.log.Timber
import javax.inject.Inject

class GetDireccionPorCardCodeTipoYLineNumUseCase @Inject constructor(
    private val socioDireccionesDao: SocioDireccionesDao
) {
    suspend operator fun invoke(cardCode: String, tipo: String, lineNum: Int) =
        try {
            socioDireccionesDao.getDireccionEdicion(cardCode, tipo, lineNum)
        } catch (e: Exception) {
            e.printStackTrace()
            DoDireccionEdicionView()
        }

}