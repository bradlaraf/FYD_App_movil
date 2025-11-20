package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject
import kotlin.Exception

class GetAllSociosPagingUseCase @Inject constructor(
    private val socioRepository: SocioRepository
) {
    suspend operator fun invoke() =
        try {
            socioRepository.getAllSocioNegocio()
        } catch (e: Exception){
            throw e
        }

}