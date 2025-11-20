package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.repositories.PedidoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPedidoDetallePorAccDocEntryUseCase @Inject constructor(
    private val repository: PedidoRepository,
    private val pedidosDetalleDao: ClientePedidosDetalleDao
) {
    suspend operator fun invoke(accDocEntry: String): List<ClientePedidoDetalle> =
        try {
            repository.getAllPedidoDetallePorAccDocEntry(accDocEntry)
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    fun getAllPedidoDetalle(): Flow<List<ClientePedidoDetalle>> =
        pedidosDetalleDao.getAllPedidoDetalle(prefsPedido.getAccDocEntry())

    fun getAllPedidoDetalleParaEditar(): Flow<List<ClientePedidoDetalle>> =
        pedidosDetalleDao.getAllPedidoDetalleParaEditar(prefsPedido.getAccDocEntry())
}