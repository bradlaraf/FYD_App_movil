package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject

class GetAllSocioNegocioNoBloqueadosUseCase @Inject constructor(
    private val socioRepository: SocioRepository
) {
    suspend operator fun invoke() =
        try {
            val clientesNoBloqueados = socioRepository.getAllSocioNegocio().filter { it.AccLocked == "N" }
            clientesNoBloqueados

        } catch (e: Exception) {
         emptyList()
        }
}