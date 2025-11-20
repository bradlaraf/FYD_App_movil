package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralCuentasB
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllGeneralCuentasBUseCase @Inject constructor(
    private val repository: GeneralRepository
) {
    suspend operator fun invoke():List<GeneralCuentasB> =
        try {
            repository.getAllGeneralCuentasB()
        } catch (e: Exception) {
            emptyList()
        }

}
