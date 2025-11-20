package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class GetAllPedidosNoMigradosUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
){
    suspend operator fun invoke() =
        try {
            pedidoRepository.getAllPedidosNoMigrados()
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}