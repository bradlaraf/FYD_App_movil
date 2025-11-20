package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.model.toDomain
import com.mobile.massiveapp.ui.view.util.getFechaAyer
import javax.inject.Inject

class GetPedidosDeAyerUseCase @Inject constructor(
    private val pedidosDao: ClientePedidosDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke() =
        try {
            pedidosDao.getAll(getFechaAyer()).map { it.toDomain() }
        } catch (e:Exception){
            errorLogDao.insert(getError("${e.message}","${e.message}"))
            emptyList()
        }
}