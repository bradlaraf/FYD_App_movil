package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import timber.log.Timber
import javax.inject.Inject

class DeleteAllPedidoDetallePorAccDocEntryUseCase @Inject constructor(
    private val repository: PedidoRepository
) {
    suspend operator fun invoke(accDocEntry: String) =
        try {
            val pedidosSinCabeceraList = repository.getAllPedidoDetallePorAccDocEntry(accDocEntry)
            pedidosSinCabeceraList.forEach { pedido ->
                repository.deleteAllPedidoDetallePorAccDocEntry(pedido.AccDocEntry)

                //Agregar la cantidad del Pedido Detalle al IsCommited del Articulo
                repository.updateIsCommitedArticulo(pedido.Quantity * -1, pedido.ItemCode)
            }
            true
        } catch (e: Exception) {
            Timber.e(e, "Error al eliminar el detalle del pedido")
            false
        }
    }

