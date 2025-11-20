package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralVendedores
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllGeneralVendedoresUseCase @Inject constructor(
    private val repository: GeneralRepository
) {
    suspend operator fun invoke():List<GeneralVendedores> =
        try{
            repository.getAllGeneralVendedores()
        } catch (e: Exception){
            emptyList()
        }
}
