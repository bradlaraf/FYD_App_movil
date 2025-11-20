package com.mobile.massiveapp.domain.sociouc.sociocontacto

import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoSocioContactos
import timber.log.Timber
import javax.inject.Inject

class GetSocioContactoPorCelularYCardCodeUseCase @Inject constructor(
    private val socioRepository: SocioRepository
) {
    suspend operator fun invoke(celular: String, cardCode: String): DoSocioContactos =
        try {
            socioRepository.getSocioContactoPorCelularYCardCode(celular, cardCode)
        } catch (e: Exception) {
            Timber.e(e, "Error en GetSocioContactoPorCelularYCardCodeUseCase")
            DoSocioContactos()
        }
}