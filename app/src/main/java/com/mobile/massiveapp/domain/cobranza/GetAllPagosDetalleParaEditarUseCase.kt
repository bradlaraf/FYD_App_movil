package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import com.mobile.massiveapp.domain.model.DoPagoDetalle
import javax.inject.Inject

class GetAllPagosDetalleParaEditarUseCase @Inject constructor(
    private val pagosDetalleDao: ClientePagosDetalleDao
) {
    suspend operator fun invoke (accDocEntry: String):List<DoPagoDetalle> =
        try {
            val pagosDetalles = pagosDetalleDao.getAllPagoDetalleXAccDocEntry(accDocEntry).filter { it.DocLine >= 1000 }
            pagosDetalles
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}