package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDao
import com.mobile.massiveapp.data.database.entities.RutaComercialEntity
import javax.inject.Inject

class SaveRutaComercialUseCase @Inject constructor(
    private val rutaDao: RutaComercialDao
) {
    suspend operator fun invoke(accDocEntry: String, fechaRuta: String, nombreVendedor: String) {
        rutaDao.insertAllData(listOf(
            RutaComercialEntity(
                AccDocEntry = accDocEntry,
                AccAction = "I",
                AccCreateDate = "",
                AccCreateHour = "",
                AccCreateUser = "",
                AccError = "N",
                Canceled = "N",
                AccNotificado = "N",
                AccFinalized = "N",
                AccMigrated = "N",
                AccMovil = "Y",
                AccUpdateDate = "",
                AccUpdateHour = "",
                AccUpdateUser = "",
                AccControl = "N",
                ObjType = -1,
                DocEntry = -1,
                DocNum = -1,
                NombreVendedor = nombreVendedor,
                FechaRuta = fechaRuta,
                Comments = ""
            )
        ))
    }
}
