package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.domain.model.DoDireccion
import javax.inject.Inject

class UpdateAddressRutaComercialDetalleUseCase @Inject constructor(
    private val rutaDetalleDao: RutaComercialDetalleDao
) {
    suspend operator fun invoke(accDocEntry: String, address: DoDireccion, lineNum: Int) =
        try {
            rutaDetalleDao.updateAddress(accDocEntry, address.Street, address.AdresType, lineNum)
            true
        } catch (e:Exception){
            e.printStackTrace()
            false
        }

}
