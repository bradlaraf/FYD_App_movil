package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.domain.model.DoDireccion
import javax.inject.Inject

class UpdateAddressRutaComercialDetalleUseCase @Inject constructor(
    private val rutaDetalleDao: RutaComercialDetalleDao
) {
    suspend operator fun invoke(accDocEntry: String, cardCode: String, address: DoDireccion) =
        try {
            rutaDetalleDao.updateAddress(accDocEntry, cardCode, address.Street, address.AdresType)
            true
        } catch (e:Exception){
            e.printStackTrace()
            false
        }

}
