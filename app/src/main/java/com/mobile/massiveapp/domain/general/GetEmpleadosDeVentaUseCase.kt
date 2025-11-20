package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.entities.GeneralVendedoresEntity
import com.mobile.massiveapp.data.repositories.GeneralRepository
import com.mobile.massiveapp.domain.getError
import javax.inject.Inject

class GetEmpleadosDeVentaUseCase @Inject constructor(
    private val generalRepository: GeneralRepository,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke() =
        try {
            generalRepository.getEmpleadoDeVentas()
        } catch (e: Exception) {
            errorLogDao.insert(getError("${e.message}","${e.message}"))
            GeneralVendedoresEntity(-1,"")
        }
}