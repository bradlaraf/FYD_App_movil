package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralDepartamentos
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllDepartamentosUseCase @Inject constructor(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(): List<GeneralDepartamentos> =
        try {
            generalRepository.getAllDepartamentos()
        } catch (e: Exception) {
            emptyList()
        }
}