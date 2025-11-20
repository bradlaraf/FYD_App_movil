package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.domain.model.DoPedidoDetalleInfo
import javax.inject.Inject

class GetPedidoDetallesInfoUseCase @Inject constructor(
    private val pedidosDetalleDao: ClientePedidosDetalleDao
) {
    suspend operator fun invoke(accDocEntry: String, lineNum: Int) =
        try {
            pedidosDetalleDao.getPedidoDetalleInfo(accDocEntry, lineNum)
        } catch (e:Exception){
            e.printStackTrace()
            DoPedidoDetalleInfo()
        }
}