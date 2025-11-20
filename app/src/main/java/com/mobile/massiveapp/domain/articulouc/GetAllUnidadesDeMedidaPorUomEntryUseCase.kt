package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import javax.inject.Inject

class GetAllUnidadesDeMedidaPorUomEntryUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
) {
    suspend operator fun invoke(uomEntry: Int) =
        try {
            articuloRepository.getAllArticuloUnidadesPorUomEntry(uomEntry)
        } catch (e: Exception){
            emptyList()
        }
}