package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoClienteSocios
import javax.inject.Inject

class GetAllSociosFiltradosPorMigradoUseCase @Inject constructor(
    private val repository: SocioRepository
) {
    suspend operator fun invoke(migrado: String): List<DoClienteSocios> =
        try {
            //Trae por el query a todos los socios no bloquados - AccLocked == N
            repository.getAllSociosFiltradoPorMigrado(migrado)
        } catch (e: Exception) {
            emptyList()
        }
}