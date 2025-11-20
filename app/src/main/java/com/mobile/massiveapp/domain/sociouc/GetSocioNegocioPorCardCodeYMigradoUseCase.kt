package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import timber.log.Timber
import javax.inject.Inject

class GetSocioNegocioPorCardCodeYMigradoUseCase @Inject constructor(
    private val socioRepository: SocioRepository
) {
    suspend operator fun invoke(cardCode: String, migrado: String): Any =

        try {
            when (migrado) {
                "Y" -> {
                    socioRepository.getClienteSocioPorCardCode(cardCode)
                }
                "N" -> {
                    socioRepository.getInfoSocioAwait(cardCode)
                }
                else -> {
                    "El socio no existe"
                }
            }

        } catch (e: Exception) {
            Timber.e(e, "Error en GetSocioNegocioPorCardCodeYMigradoUseCase")
            "El socio no existe"

        }
}