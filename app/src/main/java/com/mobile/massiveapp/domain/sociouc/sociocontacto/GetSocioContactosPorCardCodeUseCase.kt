package com.mobile.massiveapp.domain.sociouc.sociocontacto

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject

class GetSocioContactosPorCardCodeUseCase @Inject constructor(
    private val socioRepository: SocioRepository
) {

    suspend operator fun invoke(cardCode: String) =
        try {
            socioRepository.getSocioContactoPorCardCode(cardCode)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
}