package com.mobile.massiveapp.domain.configuracion

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.domain.model.DoConfiguracion
import javax.inject.Inject

class SaveConfiguracionUseCase @Inject constructor(
    private val configuracionRepository: ConfiguracionRepository
) {

    suspend operator fun invoke(configuracion: DoConfiguracion) =
        try {
            configuracionRepository.clearConfiguracion()
            configuracionRepository.saveConfiguracion(configuracion.toDatabase())
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

}