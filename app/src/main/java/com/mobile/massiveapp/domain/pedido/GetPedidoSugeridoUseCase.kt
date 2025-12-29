package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class GetPedidoSugeridoUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke(cardCode: String): Boolean {
        val pedidoSugerido = pedidoRepository.getPedidoSugeridoFromApi(cardCode)

        val pedidoPrueba: List<ClientePedidoDetalle> = List(8) { index ->

                ClientePedidoDetalle(
                "I",
                "2025-11-25",
                "15:00",
                "BLF",
                prefsPedido.getAccDocEntry(),
                "N",
                "",
                "",
                "",
                0.0,
                0,
                "Test $index",
                0.0,
                0.0,
                "Test",
                        index,
                100.0 + index,
                if(index < 5) "N" else "Y",
                "",
                "",
                "",
                "",
                24.0,
                0.0,
                0.0,
                0,
                "",
                0.0,
                "",
                0.0,
                "TEST",
                "TEST",
                0,
                0.0,
                0.0,
                "WHSCODE",
                        -1,
                        "N"
            )
        }
        return pedidoRepository.savePedidoDetalle(pedidoPrueba)
    }
}
