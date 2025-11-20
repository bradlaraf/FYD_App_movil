package com.mobile.massiveapp.domain.facturas

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.repositories.FacturasRepository
import com.mobile.massiveapp.domain.model.DoFacturaView
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllFacturasUseCase @Inject constructor(
    private val repository: FacturasRepository,
    private val facturasDao: ClienteFacturasDao
) {
    suspend operator fun invoke() =
        try {
            repository.getAllFacturasDelVendedor()
        } catch (e: Exception) {
            emptyList()
        }

    fun getAllFacturasFlow():Flow<List<DoFacturaView>> = facturasDao.getAllFacturasFlow()

}