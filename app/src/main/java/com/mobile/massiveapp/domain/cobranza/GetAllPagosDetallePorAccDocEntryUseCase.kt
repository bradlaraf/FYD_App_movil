package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import javax.inject.Inject

class GetAllPagosDetallePorAccDocEntryUseCase @Inject constructor(
    private val repository: CobranzaRepository
) {
    suspend operator fun invoke(accDocEntry: String):List<DoClientePagoDetalle> =
        try {
            val result = repository.getAllPagosDetallePorAccDocEntry(accDocEntry)
            result
        } catch (e: Exception) {
            emptyList()
        }
}