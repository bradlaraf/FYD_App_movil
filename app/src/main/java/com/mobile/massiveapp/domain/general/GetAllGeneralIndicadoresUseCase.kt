package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.LiquidacionPagoDao
import com.mobile.massiveapp.data.model.GeneralIndicadores
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllGeneralIndicadoresUseCase @Inject constructor(
    private val repository: GeneralRepository,
    private val liquidacionPagoDao: LiquidacionPagoDao
) {
    suspend operator fun invoke(): List<GeneralIndicadores> =
        try {
            repository.getAllGeneralIndicadores()

        } catch (e: Exception) {
            emptyList()
        }
}