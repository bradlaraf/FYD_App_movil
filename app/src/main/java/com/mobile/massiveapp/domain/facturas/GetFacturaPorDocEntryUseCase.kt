package com.mobile.massiveapp.domain.facturas

import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.domain.model.DoClienteFacturas
import javax.inject.Inject

class GetFacturaPorDocEntryUseCase @Inject constructor(
    private val facturaRepository: FacturasRepository
) {
    suspend operator fun invoke(docEntry: String) =
        try {
            facturaRepository.getFacturaPorDocEntry(docEntry)
        } catch (e: Exception) {
            DoClienteFacturas()
        }

}
