package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloCantidades
import javax.inject.Inject

class GetArticuloCantidadesPorItemCodeYWhsCodeUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
) {
    suspend operator fun invoke(itemCode: String, whsCode: String) =
        try {
            articuloRepository.getArticuloCantidadPorItemCodeYWhsCode(itemCode, whsCode)
        } catch (e: Exception) {
            DoArticuloCantidades()
        }
}