package com.mobile.massiveapp.domain.configuracion

import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.domain.model.DoConfiguracion
import javax.inject.Inject

class GetConfiguracionActualUseCase @Inject constructor(
    private val configuracionRepository: ConfiguracionRepository
){

    suspend operator fun invoke() =
        try {
            configuracionRepository.getConfiguracion()
        } catch (e: Exception) {
            e.printStackTrace()
            DoConfiguracion()
        }

}