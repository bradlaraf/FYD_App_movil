package com.mobile.massiveapp.domain.pedido

import android.annotation.SuppressLint
import android.util.Log
import com.mobile.massiveapp.data.database.dao.ClientePedidosDao
import com.mobile.massiveapp.data.database.dao.UsuarioDao
import com.mobile.massiveapp.data.model.PrecioFinalView
import javax.inject.Inject

class ObtenerPrecioArticuloFYDUseCase @Inject constructor(
    private val pedidosDao: ClientePedidosDao,
    private val usuarioDao: UsuarioDao
) {
    @SuppressLint("LogNotTimber")
    suspend operator fun invoke(itemCode: String, cantidad: Double, cardCode: String) =
        try {
            Log.d("Log", "$itemCode-$cardCode")
            pedidosDao.obtenerPrecioFinal(
                articulo = itemCode,
                cardCode = cardCode
            )
        } catch (e:Exception){
            e.printStackTrace()
            PrecioFinalView()
        }
}