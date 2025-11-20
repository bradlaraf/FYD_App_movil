package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.data.repositories.SocioDireccionesRepository
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import javax.inject.Inject

class SaveDireccionUseCase @Inject constructor(
    private val direccionesRepository: SocioDireccionesRepository
) {

    suspend operator fun invoke(direccion: DoSocioDirecciones): Boolean =
        try {
            direccionesRepository.saveDireccion(direccion)
        } catch (e: Exception) {
            false
        }
}