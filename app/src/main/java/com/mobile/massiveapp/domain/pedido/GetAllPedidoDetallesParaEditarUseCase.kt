package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class GetAllPedidoDetallesParaEditarUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke( accDocEntry: String ):List<ClientePedidoDetalle> =
        try {
            val pedidoDetalles = pedidoRepository.getAllPedidoDetallePorAccDocEntry(accDocEntry).filter { it.LineNum >= 1000 }
            pedidoDetalles
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}