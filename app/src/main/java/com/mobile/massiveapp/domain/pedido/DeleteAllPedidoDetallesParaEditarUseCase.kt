package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class DeleteAllPedidoDetallesParaEditarUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke(accDocEntry: String):Boolean =
        try {
            val pedidoDetalles = pedidoRepository.getAllPedidoDetallePorAccDocEntry(accDocEntry).filter { it.LineNum >= 1000 }
            pedidoDetalles.forEach {
                pedidoRepository.deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, it.LineNum)
                pedidoRepository.updateIsCommitedArticulo(it.Quantity * -1, it.ItemCode)
            }
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}