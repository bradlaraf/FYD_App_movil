package com.mobile.massiveapp.domain.sociouc.sociocontacto

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject

class DeleteSocioContactoPorCelularYCardCodeUseCase @Inject constructor(
    private val socioRepository: SocioRepository
) {
    suspend operator fun invoke(celular: String, cardCode: String):Boolean =
        try {
            socioRepository.deleteSocioContactoPorCelularYCardCode(celular, cardCode)
        } catch (e: Exception) {
            false
        }

}