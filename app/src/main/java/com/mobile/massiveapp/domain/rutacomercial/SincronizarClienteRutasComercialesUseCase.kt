package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import javax.inject.Inject

class SincronizarClienteRutasComercialesUseCase @Inject constructor(
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository
) {
    suspend operator fun invoke(fechaInicio: String, fechaFin: String): Boolean = try {
        val usuario = loginRepository.getUsuarioFromDatabase()
        val configuracion = configuracionRepository.getConfiguracion()
        val url = getUrlFromConfiguracion(configuracion)
        datosMaestrosRepository.sincronizarClienteRutasComerciales(
            configuracion, usuario, url, fechaInicio, fechaFin
        )
        true
    } catch (e: Exception) { false }
}
