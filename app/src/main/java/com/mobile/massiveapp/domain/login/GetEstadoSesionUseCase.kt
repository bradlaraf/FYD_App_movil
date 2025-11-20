package com.mobile.massiveapp.domain.login

import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import javax.inject.Inject

class GetEstadoSesionUseCase @Inject constructor(
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,

    private val datosMaestrosService: DatosMaestrosService
) {

    suspend operator fun invoke():Any =
        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)

            val estadoSesion = datosMaestrosService.getEstadoSesion(
                usuario,
                configuracion,
                url,
                10L
            )
            estadoSesion
        } catch (e:Exception){
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = -1)
        }
}