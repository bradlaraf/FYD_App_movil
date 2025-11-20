package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class DeleteAllPedidoDetalleSinCabeceraUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
){
    suspend operator fun invoke(): Boolean =
        try {
            val pedidosSinCabeceraList = pedidoRepository.getAllAccDocEntryPedidosDetalleSinCabecera()
            pedidosSinCabeceraList.forEach { pedido ->
                pedidoRepository.deleteAllPedidoDetallePorAccDocEntry(pedido.AccDocEntry)

                //Agregar la cantidad del Pedido Detalle al IsCommited del Articulo
                pedidoRepository.updateIsCommitedArticulo(pedido.Quantity * -1, pedido.ItemCode)
            }

            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }

}