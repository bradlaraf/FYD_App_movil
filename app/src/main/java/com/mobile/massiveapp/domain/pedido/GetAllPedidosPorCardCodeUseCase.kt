package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class GetAllPedidosPorCardCodeUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke(cardCode: String) =
        try {
            pedidoRepository.getAllPedidosPorCardCode(cardCode)
        } catch (e: Exception){
            emptyList()
        }
}