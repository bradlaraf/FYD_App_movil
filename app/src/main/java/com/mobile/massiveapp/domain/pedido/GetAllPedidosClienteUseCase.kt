package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.domain.model.DoClientePedido
import javax.inject.Inject

class GetAllPedidosClienteUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
){
    suspend operator fun invoke():List<DoClientePedido> =
        try {
            pedidoRepository.getAllPedidosDelDia().filter { it.AccMigrated == "Y"}
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}