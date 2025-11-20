package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class SavePedidoDetalleParaEditarUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    suspend operator fun invoke(clientePedidoDetalle: ClientePedidoDetalle):Boolean =
        try {
            clientePedidoDetalle.LineNum + 1000
            repository.savePedidoDetalle(clientePedidoDetalle)
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}