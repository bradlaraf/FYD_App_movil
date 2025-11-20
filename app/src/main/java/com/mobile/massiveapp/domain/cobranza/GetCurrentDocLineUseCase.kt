package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.repositories.CobranzaRepository
import javax.inject.Inject

class GetCurrentDocLineUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository
) {
    suspend operator fun invoke(accDocEntry: String) =
        try {
            cobranzaRepository.getCurrentDocLine(accDocEntry)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }

}