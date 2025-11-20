package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralImpuestos
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetImpuestoDefaultUseCase @Inject constructor(
    private val generalRepository: GeneralRepository
) {
    suspend operator fun invoke(): GeneralImpuestos =
        try {
            generalRepository.getImpuestoDefault()
        } catch (e: Exception) {
            GeneralImpuestos()
        }
}