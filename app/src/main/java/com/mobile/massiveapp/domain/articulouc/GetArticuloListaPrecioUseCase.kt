package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloListaPrecios
import java.lang.Exception
import javax.inject.Inject

class GetArticuloListaPrecioUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
) {
    suspend operator fun invoke(): List<DoArticuloListaPrecios> =
        try {
            articuloRepository.getAllArticulosListaPreciosFromDatabase()
        } catch (e: Exception) {
            emptyList()
        }
}