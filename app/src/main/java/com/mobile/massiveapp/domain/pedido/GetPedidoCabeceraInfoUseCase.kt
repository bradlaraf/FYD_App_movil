package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.domain.model.DoPedidoInfoView
import javax.inject.Inject

class GetPedidoCabeceraInfoUseCase @Inject constructor(
    private val pedidoDao: ClientePedidosDao
) {
    suspend operator fun invoke(accDocEntry: String) =
        try {
            pedidoDao.getPedidoInfo(accDocEntry)
        } catch (e:Exception){
            e.printStackTrace()
            DoPedidoInfoView()
        }
}