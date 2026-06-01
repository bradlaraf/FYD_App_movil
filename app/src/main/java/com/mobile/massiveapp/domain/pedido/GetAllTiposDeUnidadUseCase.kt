package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.database.dao.ArticuloDao
import com.mobile.massiveapp.domain.model.TipoUnidadView
import javax.inject.Inject

class GetAllTiposDeUnidadUseCase @Inject constructor(
    private val articuloDao: ArticuloDao
) {
    suspend operator fun invoke(itemCode: String):List<TipoUnidadView> {

        val articulo = articuloDao.getArticulo(itemCode = itemCode)

        return listOf(
                TipoUnidadView(Code = "SalUnitMsr", Name = articulo.SalUnitMsr),
                TipoUnidadView(Code = "SalPackMsr", Name = articulo.SalPackMsr)
        )
    }
}
