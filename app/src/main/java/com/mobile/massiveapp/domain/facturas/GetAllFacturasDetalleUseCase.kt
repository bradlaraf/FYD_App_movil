package com.mobile.massiveapp.domain.facturas

import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class GetAllFacturasDetalleUseCase @Inject constructor(
    private val facturasRepository: FacturasRepository
) {
    suspend operator fun invoke(docEntry: Int) =
        try {
            facturasRepository.getAllFacturaDetallePorDocEntry(docEntry).map { it.toDomain() }
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}