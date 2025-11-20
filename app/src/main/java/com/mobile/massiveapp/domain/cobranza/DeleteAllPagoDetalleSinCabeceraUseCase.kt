package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import javax.inject.Inject

class DeleteAllPagoDetalleSinCabeceraUseCase @Inject constructor(
    private val pagosDetalleDao: ClientePagosDetalleDao,
    private val facturasDao: ClienteFacturasDao
) {
    suspend operator fun invoke(): Boolean =
        try {
            pagosDetalleDao.deleteAllPagosDetallesSinCabecera()
            facturasDao.setEditPtdToDeafult()
            true
        } catch (e: Exception) {
            false
        }
}
