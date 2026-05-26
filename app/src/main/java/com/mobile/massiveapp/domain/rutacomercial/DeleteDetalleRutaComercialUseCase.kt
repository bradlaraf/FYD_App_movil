package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import javax.inject.Inject

class DeleteDetalleRutaComercialUseCase @Inject constructor(
    private val rutaDetalleDao: RutaComercialDetalleDao
) {
    suspend operator fun invoke(accDocEntry: String, cardCode: String) {
        rutaDetalleDao.deleteByCardCode(accDocEntry, cardCode)
    }
}
