package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.data.repositories.SocioDireccionesRepository
import timber.log.Timber
import javax.inject.Inject

class DeleteUnaDireccionPorCardCodeYTipoUseCase @Inject constructor(
    private val socioDireccionesRepository: SocioDireccionesRepository
){
    suspend operator fun invoke(cardCode: String, tipo: String) =
        try {
            socioDireccionesRepository.deleteUnaDireccionPorCardCodeYTipo(cardCode, tipo)
        } catch (e: Exception) {
            Timber.e(e, "Error en DeleteUnaDireccionPorCardCodeYTipoUseCase")
            false
        }

}