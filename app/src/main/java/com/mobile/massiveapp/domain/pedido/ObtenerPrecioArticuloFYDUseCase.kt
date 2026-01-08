package com.mobile.massiveapp.domain.pedido

import android.annotation.SuppressLint
import android.util.Log
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.model.PrecioFinalView
import javax.inject.Inject

class ObtenerPrecioArticuloFYDUseCase @Inject constructor(
    private val pedidosDao: ClientePedidosDao
) {
    @SuppressLint("LogNotTimber")
    suspend operator fun invoke(itemCode: String, cardCode: String) =
        try {
            pedidosDao.obtenerPrecioFinal(
                articulo = itemCode,
                cardCode = cardCode
            )?:PrecioFinalView()
        } catch (e:Exception){
            e.printStackTrace()
            PrecioFinalView()
        }
}