package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoUnidadMedidaInfo
import com.mobile.massiveapp.ui.view.util.format
import javax.inject.Inject

class GetUnidadMedidaYEquivalenciaUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
) {
    suspend operator fun invoke(itemCode: String, unidadMedida: String, listNum: Int): DoUnidadMedidaInfo =
        try {
            val precio = articuloRepository.getArticuloPrecioParaPedido(itemCode, listNum, unidadMedida)
            precio.PrecioFinal.format(2)
            precio
        } catch (e: Exception) {
            DoUnidadMedidaInfo()
        }
}