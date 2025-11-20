package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.repositories.PedidoRepository
import javax.inject.Inject

class SavePedidoDetalleUseCase @Inject constructor(
    private val repository: PedidoRepository,
    private val pedidosDetalleDao: ClientePedidosDetalleDao
) {
    /** Solo puede un itemCode por pedido **/
    suspend operator fun invoke(clientePedidoDetalle: ClientePedidoDetalle):Boolean =
        try {
            /*val pagoDetalleBd = clientePagosDetalleDao.getPagoDetalle(cobranzaDetalle.AccDocEntry, cobranzaDetalle.DocEntry)

            if (pagoDetalleBd.isNotEmpty()){
                val docLineActual = pagoDetalleBd[0].DocLine
                cobranzaDetalle.DocLine = docLineActual
                clientePagosDetalleDao.update(cobranzaDetalle.toDatabase())
            } else {
                cobranzaRepository.saveCobranzaDetalle(cobranzaDetalle)
            }
            true*/

            val editMode = clientePedidoDetalle.LineNum >= 1000

            val pedidosDetalleRegistrados =
                if (editMode) pedidosDetalleDao.getAllPedidoDetalleEdicion(clientePedidoDetalle.AccDocEntry, clientePedidoDetalle.ItemCode)
                else pedidosDetalleDao.getAllPedidoDetalle(clientePedidoDetalle.AccDocEntry, clientePedidoDetalle.ItemCode)

            var isCommitedToRestore = 0.0

            if (pedidosDetalleRegistrados.isNotEmpty()){
                val listaUnitmsr = pedidosDetalleRegistrados.map { it.UnitMsr }

                if (clientePedidoDetalle.UnitMsr in listaUnitmsr){
                    val pedidoDetalleFiltrado = pedidosDetalleRegistrados.filter { it.UnitMsr == clientePedidoDetalle.UnitMsr }
                    isCommitedToRestore += pedidoDetalleFiltrado.first().Quantity
                    val lineNumActual = pedidoDetalleFiltrado.first().LineNum
                    clientePedidoDetalle.LineNum = lineNumActual
                    pedidosDetalleDao.update(clientePedidoDetalle.toDatabase())
                } else {
                    repository.savePedidoDetalle(clientePedidoDetalle)
                }

            } else {
                repository.savePedidoDetalle(clientePedidoDetalle)
            }


            //Agregar el onCommited al articulo del Pedido Detalle
            repository.updateIsCommitedArticulo((clientePedidoDetalle.Quantity + isCommitedToRestore), clientePedidoDetalle.ItemCode)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }


}
