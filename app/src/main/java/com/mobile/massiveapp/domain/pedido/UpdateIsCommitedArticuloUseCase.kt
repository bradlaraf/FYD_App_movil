package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class UpdateIsCommitedArticuloUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
){
    suspend operator fun invoke(cantidad: Double, itemCode: String): Boolean =
        try {
            pedidoRepository.updateIsCommitedArticulo(cantidad, itemCode)
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}