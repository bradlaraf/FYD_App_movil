package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoClienteSocios
import javax.inject.Inject

class InsertarSocioNegocioUseCase @Inject constructor(
    private val repository: SocioRepository
) {
    suspend operator fun invoke(doClienteSocios: DoClienteSocios){
        repository.insertSocio(doClienteSocios.toDatabase())
    }
}