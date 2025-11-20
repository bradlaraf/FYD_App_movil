package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import javax.inject.Inject

class DuplicateAllPagoDetallesParaActualizacionUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val facturasDao: ClienteFacturasDao
) {
    suspend operator fun invoke(accDocEntry: String): Boolean =
        try {
            val pagoDetalles = cobranzaRepository.getAllPagosDetallePorAccDocEntry(accDocEntry)

            if (pagoDetalles.isNotEmpty()){
                val facturasAEditar = facturasDao.getAllFacturasPorAccDocEntry(accDocEntry)
                facturasAEditar.forEach {
                    facturasDao.setEditPtdEqualPaidToCode(it.DocEntry)
                }
            }


            pagoDetalles.forEach {
                it.DocLine = it.DocLine + 1000
                cobranzaRepository.saveCobranzaDetalle(it)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}