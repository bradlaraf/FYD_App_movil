package com.mobile.massiveapp.domain.configuracion

import com.mobile.massiveapp.data.database.dao.ConfiguracionDao
import javax.inject.Inject

class SaveLimitesConfigurationUseCase @Inject constructor(
    private val configuracionDao: ConfiguracionDao
) {
    suspend operator fun invoke(usarLimite: Boolean,articulo: Int, factura: Int, cliente: Int): Boolean {
        configuracionDao.updateLimites(usarLimite, articulo, factura, cliente)
        return true
    }
}