package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllProvinciasUseCase @Inject constructor(
    private val generalRepository: GeneralRepository
){
    suspend operator fun invoke(code:String) =
        try {
            generalRepository.getAllProvincias(code)
        } catch (e: Exception) {
            emptyList()
        }

}