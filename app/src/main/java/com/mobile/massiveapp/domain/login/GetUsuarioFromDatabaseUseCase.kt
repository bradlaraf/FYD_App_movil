package com.mobile.massiveapp.domain.login

import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.model.DoUsuario
import javax.inject.Inject

class GetUsuarioFromDatabaseUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke():DoUsuario =
        try {
            loginRepository.getUsuarioFromDatabase()
        } catch (e: Exception) {
            e.printStackTrace()
            DoUsuario()
        }
}