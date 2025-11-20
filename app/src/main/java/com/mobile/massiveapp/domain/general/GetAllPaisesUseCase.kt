package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralPaises
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllPaisesUseCase @Inject constructor(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(): List<GeneralPaises> =
        try {
            generalRepository.getAllPaises()
        } catch (e: Exception) {
            emptyList()
        }

}