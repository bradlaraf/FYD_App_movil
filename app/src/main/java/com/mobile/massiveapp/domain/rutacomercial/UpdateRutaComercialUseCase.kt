package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDao
import javax.inject.Inject

class UpdateRutaComercialUseCase @Inject constructor(
    private val rutaDao: RutaComercialDao
) {
    suspend operator fun invoke(accDocEntry: String, fechaRuta: String) {
        rutaDao.updateFechaRuta(accDocEntry, fechaRuta)
    }
}
