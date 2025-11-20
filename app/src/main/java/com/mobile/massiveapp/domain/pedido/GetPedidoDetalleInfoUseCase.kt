package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ClientePedidosDetalleDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.model.DoPedidoDetalle
import javax.inject.Inject

class GetPedidoDetalleInfoUseCase @Inject constructor(
    private val pedidosDetalleDao: ClientePedidosDetalleDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke(accDocEntry: String, lineNum: Int) =
        try {
            pedidosDetalleDao.getDetalleInfo(accDocEntry, lineNum)
        } catch (e:Exception){
            errorLogDao.insert(getError("${e.message}","${e.message}"))
            DoPedidoDetalle()
        }
}