package com.mobile.massiveapp.domain.pedido

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloPrecios
import com.mobile.massiveapp.ui.view.util.format
import javax.inject.Inject

class GetArticuloPrecioPorItemCodeYPriceListUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
) {
    suspend operator fun invoke(itemCode: String, priceList: Int) =
        try {
            val precio = articuloRepository.getArticuloPrecioPorItemCodeYPriceList(itemCode, priceList)
            precio.Price = precio.Price.format(2)
            precio
        } catch (e: Exception) {
            DoArticuloPrecios()
        }


}