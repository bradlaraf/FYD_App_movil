package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class UpdateAllPedidosDetalleUseCase @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val pedidosDetallesDao: ClientePedidosDetalleDao
) {
    suspend operator fun invoke( accDocEntry: String ):Boolean =
        try {
            val pedidoDetalles = pedidosDetallesDao.getAllDetallesEdicion(accDocEntry).map { it.toModel() }

            if (pedidoDetalles.isNotEmpty()){
                pedidoRepository.deleteAllPedidoDetallePorAccDocEntry(accDocEntry)
            }
            pedidoDetalles.forEach {
                it.LineNum = it.LineNum - 1000
                pedidoRepository.savePedidoDetalle(it)
            }
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}