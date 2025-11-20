package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllGeneralImpuestosUseCase @Inject constructor(
    private val repository: GeneralRepository
) {
    suspend operator fun invoke() =
        try {
            repository.getAllGeneralImpuestos()
        } catch (e: Exception) {
            emptyList()
        }

}