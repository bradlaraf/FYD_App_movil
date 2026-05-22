package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity
import javax.inject.Inject

class SaveRutaComercialDetalleUseCase @Inject constructor(
    private val rutaDetalleDao: RutaComercialDetalleDao,
    private val socioDireccionesDao: SocioDireccionesDao
) {
    suspend operator fun invoke(accDocEntry: String, cardCode: String): Boolean {
        return try {
            val docLine = rutaDetalleDao.countByAccDocEntry(accDocEntry)
            val address = socioDireccionesDao
                .getDireccionesPorTipoYCardCode(cardCode, "B")
                .firstOrNull()?.Street ?: ""
            rutaDetalleDao.insertAllData(
                listOf(
                    RutaComercialDetalleEntity(
                        AccDocEntry = accDocEntry,
                        DocLine = docLine,
                        AccAction = "I",
                        AccCreateDate = "",
                        AccCreateHour = "",
                        AccCreateUser = "",
                        AccUpdateDate = "",
                        AccUpdateHour = "",
                        AccUpdateUser = "",
                        AccMigrated = "N",
                        AccControl = "N",
                        Status = "P",
                        CardCode = cardCode,
                        Address = address,
                        AddressType = "B",
                        Comments = "",
                        ObjType = -1,
                        DocEntry = -1
                    )
                )
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}
