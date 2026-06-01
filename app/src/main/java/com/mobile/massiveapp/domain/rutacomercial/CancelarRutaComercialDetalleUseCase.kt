package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import javax.inject.Inject

class CancelarRutaComercialDetalleUseCase @Inject constructor(
    private val rutaComercialDetalleDao: RutaComercialDetalleDao
) {
    suspend operator fun invoke(docLine: Int, accDocEntry: String) =
        try {
            rutaComercialDetalleDao.cancelarRutaDetalle(docLine, accDocEntry)
            true
        } catch (e: Exception) {
            false
        }
}
