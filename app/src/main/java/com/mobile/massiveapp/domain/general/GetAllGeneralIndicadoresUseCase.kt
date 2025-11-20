package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralIndicadores
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllGeneralIndicadoresUseCase @Inject constructor(
    private val repository: GeneralRepository
) {
    suspend operator fun invoke(): List<GeneralIndicadores> =
        try {
            repository.getAllGeneralIndicadores()
        } catch (e: Exception) {
            emptyList()
        }
}