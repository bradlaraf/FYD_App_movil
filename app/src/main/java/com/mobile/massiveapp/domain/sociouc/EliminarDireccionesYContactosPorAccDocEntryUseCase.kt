package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject

class EliminarDireccionesYContactosPorAccDocEntryUseCase @Inject constructor(
    private val repository: SocioRepository
){
    suspend operator fun invoke(accDocEntry: String): Boolean {
        return try {
            repository.deleteAllSocioContactosPorAccDocEntry(accDocEntry)
            repository.deleteAllSocioDireccionesPorAccDocEntry(accDocEntry)
            true
        } catch (e: Exception) {
            println("Error en EliminarDireccionesYContactosPorAccDocEntryUseCase: ${e.message}")
            false
        }
    }
}