package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class GetPedidoPorAccDocEntryUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke(accDocEntry: String) =
        try {
            pedidoRepository.getPedidoPorAccDocEntry(accDocEntry)
        } catch (e: Exception){
            e.printStackTrace()
            throw Exception(e.message)
        }

}
