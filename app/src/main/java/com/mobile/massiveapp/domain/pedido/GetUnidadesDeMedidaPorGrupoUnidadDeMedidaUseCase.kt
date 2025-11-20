package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import javax.inject.Inject

class GetUnidadesDeMedidaPorGrupoUnidadDeMedidaUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
){
    suspend operator fun invoke(itemCode: String) =
        try {
            articuloRepository.getArticuloUnidadMedidaPorGrupUnidadMedida(itemCode)
        } catch (e: Exception){
            emptyList()
        }
}