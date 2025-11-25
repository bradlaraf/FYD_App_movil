package com.mobile.massiveapp.data.util

import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoUsuario
import javax.inject.Inject

class GetConfigInfoForCalls @Inject constructor(
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository
) {
    data class ConfigInfoForCalls(
        val configuracion: DoConfiguracion,
        val usuario: DoUsuario,
        val url: String
    )

    suspend operator fun invoke(): ConfigInfoForCalls {
        val configuracion = configuracionRepository.getConfiguracion()
        val usuario = loginRepository.getUsuarioFromDatabase()
        val url = getUrlFromConfiguracion(configuracion)

        return ConfigInfoForCalls(
            configuracion = configuracion,
            usuario = usuario,
            url = url
        )
    }
}