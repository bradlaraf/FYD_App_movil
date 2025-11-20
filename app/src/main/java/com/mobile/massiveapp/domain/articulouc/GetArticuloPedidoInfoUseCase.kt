package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloPedidoInfo
import javax.inject.Inject

class GetArticuloPedidoInfoUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
) {
    suspend operator fun invoke(itemCode: String): DoArticuloPedidoInfo =
        try {
            /*val articuloSuomEntry = articuloRepository.getArticuloPorItemCode(itemCode).SuoMEntry

            val response = if (articuloSuomEntry != -1){
                articuloRepository.getArticuloInfoPedidoConUnidadMedida(itemCode)
            } else {
                articuloRepository.getArticuloInfoPedidoSinUnidadMedida(itemCode)
            }*/

            articuloRepository.getArticuloInfoPedidoConUnidadMedida(itemCode)

        } catch (e: Exception){
            e.printStackTrace()
            DoArticuloPedidoInfo()
        }

}