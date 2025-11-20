package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoClienteSocios
import timber.log.Timber
import javax.inject.Inject

class GetSocioNegocioParaEditarUseCase @Inject constructor(
    private val socioNegocioRepository: SocioRepository
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            socioNegocioRepository.getSocioNegocioPorCardCode(cardCode)
        } catch (e: Exception) {
            Timber.tag("GetSocioNegocioParaEditarUseCase").e(e)
            DoClienteSocios()
        }
}