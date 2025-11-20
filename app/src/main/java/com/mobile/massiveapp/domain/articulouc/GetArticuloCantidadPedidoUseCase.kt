package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import javax.inject.Inject

class GetArticuloCantidadPedidoUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
){
    suspend operator fun invoke(itemCode: String, unidadMedida: String, whsCode: String) =
        try {
            articuloRepository.getArticuloCantidadParaPedido(itemCode, unidadMedida, whsCode)
        } catch (e:Exception){
            e.printStackTrace()
            0.0
        }

}