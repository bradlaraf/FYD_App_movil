package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.MassiveApp.Companion.prefsPedido
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.domain.model.ArticuloPedido
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.formato
import com.mobile.massiveapp.ui.view.util.getFechaActual
import javax.inject.Inject

class GetPrecioArticuloUseCase @Inject constructor(
    private val pedidoDao: ClientePedidosDao,
    private val clientePedidosDetalleDao: ClientePedidosDetalleDao
){
    suspend operator fun invoke(cantidad: Int, itemCode: String, listaPrecio: Int): ArticuloPedido =
        try {
            val fecha = getFechaActual()
            val cardCode = prefsPedido.getCardCode()
            val priceDec = pedidoDao.getPriceDec()

            var precio = pedidoDao.getPrecioArticulo(
                                        fecha = fecha,
                                        articulo = itemCode,
                                        listaPrecio = listaPrecio,
                                        cantidad = cantidad,
                                        cardCode = cardCode,
                                        priceDec = priceDec
            )
            precio.PrecioIGV = pedidoDao.getPrecioAftVat(precio.Precio)
            precio
        } catch (e:Exception){
            e.printStackTrace()
            ArticuloPedido()
        }

}