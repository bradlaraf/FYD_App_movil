package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.data.repositories.SocioDireccionesRepository
import timber.log.Timber
import javax.inject.Inject

class GetLineNumPorCardCodeYTipoUseCase @Inject constructor(
    private val direccionesRepository: SocioDireccionesRepository
) {
    suspend operator fun invoke(cardCode: String, tipo: String): Int =
        try {
            direccionesRepository.getLineNumPorCardCodeYTipo(cardCode, tipo)
        } catch (e: Exception) {
            Timber.e(e, "Error al obtener el LineNum por CardCode y Tipo")
            -1
        }
}
