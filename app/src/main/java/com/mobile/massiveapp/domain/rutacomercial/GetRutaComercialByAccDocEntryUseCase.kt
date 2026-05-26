package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDao
import com.mobile.massiveapp.domain.model.DoRutaComercial
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class GetRutaComercialByAccDocEntryUseCase @Inject constructor(
    private val rutaDao: RutaComercialDao
) {
    suspend operator fun invoke(accDocEntry: String): DoRutaComercial? {
        return rutaDao.getByAccDocEntry(accDocEntry)?.toDomain()
    }
}
