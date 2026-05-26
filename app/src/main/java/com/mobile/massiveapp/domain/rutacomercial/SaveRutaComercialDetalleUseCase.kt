package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
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
                        LineNum = docLine,
                        AccAction = "I",
                        AccCreateDate = getFechaActual(),
                        AccCreateHour = getHoraActual(),
                        AccCreateUser = prefsApp.getUserCode(),
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
