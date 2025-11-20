package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import javax.inject.Inject

class InsertarUnaDireccionUseCase @Inject constructor(
    private val repository: SocioRepository
) {

    suspend operator fun invoke(direccionNueva: DoSocioDirecciones): Boolean =
        try {
            repository.insertSocioDirecciones(direccionNueva.toDatabase())
        } catch (e: Exception) {
            println("Error en InsertarUnaDireccionUseCase: ${e.message}")
            false
        }

}