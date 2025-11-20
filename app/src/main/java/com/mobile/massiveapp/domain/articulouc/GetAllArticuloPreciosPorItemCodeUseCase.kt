package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloPrecios
import javax.inject.Inject

class GetAllArticuloPreciosPorItemCodeUseCase @Inject constructor(
    private val repository: ArticuloRepository
) {
    suspend operator fun invoke(itemCode: String): List<DoArticuloPrecios> =
        try {
            repository.getAllArticuloPreciosPorItemCode(itemCode)
        } catch (e: Exception){
            emptyList()
        }

}

