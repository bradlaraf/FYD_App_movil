package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class GetUnPedidoDetallePorAccDocEntryYLineNumUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke(accDocEntry: String, lineNum: Int): ClientePedidoDetalle =
        try {
            pedidoRepository.getUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry, lineNum)
        } catch (e: Exception) {
            ClientePedidoDetalle()
        }

}