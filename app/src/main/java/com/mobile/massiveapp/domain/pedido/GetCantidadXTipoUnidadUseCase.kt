package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import javax.inject.Inject

class GetCantidadXTipoUnidadUseCase @Inject constructor(
    private val articuloDao: ArticuloDao
) {
    suspend operator fun invoke(itemCode: String, tipoUnidad: String, cantidad: Double): Double {
        return try {
            articuloDao.getCantidadXTipoUnidad(
                itemCode = itemCode,
                tipoUnidad = tipoUnidad,
                cantidad = cantidad
            ) ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }
}