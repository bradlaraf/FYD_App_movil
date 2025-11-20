package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralCondiciones
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetCondicionDePagoDefaultUseCase @Inject constructor(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(cardCode: String): GeneralCondiciones =
        try {
            generalRepository.getCondicionDePagoDefault(cardCode)
        } catch (e: Exception) {
            GeneralCondiciones()
        }
}