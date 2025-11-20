package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import javax.inject.Inject

class GetCobranzaDetallePorAccDocEntryYLineNumUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository
) {
    suspend operator fun invoke (accDocEntry: String, lineNum: String) =
        try {
            cobranzaRepository.getCobranzaDetallePorAccDocEntryYLineNum(accDocEntry, lineNum)
        } catch (e: Exception) {
            e.printStackTrace()
            DoClientePagoDetalle()
        }

}