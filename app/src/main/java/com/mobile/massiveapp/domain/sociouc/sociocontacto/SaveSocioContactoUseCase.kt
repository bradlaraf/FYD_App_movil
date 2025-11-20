package com.mobile.massiveapp.domain.sociouc.sociocontacto

import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoSocioContactos
import javax.inject.Inject

class SaveSocioContactoUseCase @Inject constructor(
    private val socioRepository: SocioRepository
) {
    suspend operator fun invoke(contactoNuevo: DoSocioContactos): Boolean =
        try {
            socioRepository.saveSocioContacto(contactoNuevo)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}