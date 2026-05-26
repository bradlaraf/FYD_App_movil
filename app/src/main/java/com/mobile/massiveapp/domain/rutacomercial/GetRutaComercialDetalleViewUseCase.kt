package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.MassiveApp.Companion.prefsRutaComercial
import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRutaComercialDetalleViewUseCase @Inject constructor(
    private val rutaDetalleDao: RutaComercialDetalleDao
) {
    fun getAllDetallesViewFlow(): Flow<List<DoRutaComercialDetalleView>> = rutaDetalleDao.getAllDetalleViewFlow(prefsRutaComercial.getAccDocEntry())
}
