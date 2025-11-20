package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralCondiciones
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllGeneralCondicionesUseCase @Inject constructor(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(): List<GeneralCondiciones> =
        try {
            generalRepository.getAllGeneralCondiciones()
        } catch (e: Exception) {
            emptyList()
        }
}