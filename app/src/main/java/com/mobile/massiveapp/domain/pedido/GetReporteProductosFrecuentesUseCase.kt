package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.domain.model.DoReporteProductosFrecuentes
import javax.inject.Inject

class GetReporteProductosFrecuentesUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository
) {
    suspend operator fun invoke(cardCode: String): List<DoReporteProductosFrecuentes> {
        return try {
            pedidoRepository.getReporteProductosFrecuentesFromApi(cardCode)
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
