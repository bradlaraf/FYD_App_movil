package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.domain.model.DoClientePago
import javax.inject.Inject

class GetPagoCabeceraPorAccDocEntryUseCase @Inject constructor(
    private val repository: CobranzaRepository
){
    suspend operator fun invoke(accDocEntry: String): DoClientePago =
        try {
            repository.getPagoCabeceraPorAccDocEntry(accDocEntry)
        } catch (e: Exception) {
            DoClientePago()
        }

}