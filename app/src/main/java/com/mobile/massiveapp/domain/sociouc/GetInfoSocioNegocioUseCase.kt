package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject


class GetInfoSocioNegocioUseCase @Inject constructor(
    private val repository: SocioRepository
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            repository.getInfoSocio(cardCode)
        } catch (e: Exception) {
            throw e
        }

}
