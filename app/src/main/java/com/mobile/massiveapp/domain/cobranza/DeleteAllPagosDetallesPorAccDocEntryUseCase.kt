package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import javax.inject.Inject

class DeleteAllPagosDetallesPorAccDocEntryUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val facturasDao: ClienteFacturasDao
) {
    suspend operator fun invoke(accDocEntry: String):Boolean =
        try {
            facturasDao.setEditPtdToDeafult()
            cobranzaRepository.deleteAllPagosDetallesPorAccDocEntry(accDocEntry)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}