package com.mobile.massiveapp.domain.login

import android.content.Context
import com.mobile.massiveapp.MassiveApp.Companion.prefsApp
import com.mobile.massiveapp.data.model.Usuario
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository
) {
    suspend operator fun invoke(version: String, usuario:String, password: String, context:Context): DoError =
        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            val login = loginRepository.login(version, usuario, password, context, configuracion, url)

            var errorCodigo = 0
            val mensaje = when (login){
                is Usuario -> {
                    loginRepository.saveUsuarioInDatabase( login )
                    prefsApp.saveUserName(login.Name)
                    "Login exitoso"
                }
                is DoError -> {
                    errorCodigo = login.ErrorCodigo
                    login.ErrorMensaje
                }
                else -> { "Sin conexión" }
            }

            DoError(
                mensaje,
                errorCodigo
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DoError("Login fallido", -1)
        }
}