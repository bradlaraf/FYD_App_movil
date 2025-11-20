package com.mobile.massiveapp.domain.login

import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val configuracionRepository: ConfiguracionRepository
) {
    suspend operator fun invoke(): DoError =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            loginRepository.cerrarSesion(usuario, configuracion, url)

        } catch (e: Exception) {
            e.printStackTrace()
            DoError("", -1)
        }
}
