package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.repositories.GeneralRepository
import timber.log.Timber
import javax.inject.Inject

class GetAllGeneralMonedosUseCase @Inject constructor(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke() =
        try {
            generalRepository.getAllGeneralMonedas()
        } catch (e: Exception) {
            Timber.d("Error en GetAllGeneralMonedosUseCase: ${e.message}")
            emptyList()
        }
}