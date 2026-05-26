package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.data.database.entities.RutaComercialDetalleEntity
import com.mobile.massiveapp.domain.model.DoRutaComercialDetalleView
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual
import javax.inject.Inject

class UpdateDocLinesRutaComercialUseCase @Inject constructor(
    private val rutaDetalleDao: RutaComercialDetalleDao
) {
    suspend operator fun invoke(detalles: List<DoRutaComercialDetalleView>) {
        if (detalles.isEmpty()) return
        rutaDetalleDao.reorderDocLines(
            currentDocLines = detalles.map { it.LineNum },
            accDocEntry = detalles.first().AccDocEntry,
            horaUpdate = getHoraActual(),
            fechaUpdate = getFechaActual(),
            userUpdate = prefsApp.getUserCode()
        )
    }
}
