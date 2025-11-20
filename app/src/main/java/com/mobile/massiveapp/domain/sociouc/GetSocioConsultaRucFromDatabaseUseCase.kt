package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoConsultaDocumento
import javax.inject.Inject

class GetSocioConsultaRucFromDatabaseUseCase @Inject constructor(
    private val repository: SocioRepository
) {
    suspend operator fun invoke(): DoConsultaDocumento =
        try {
            repository.getConsultaRucFromDatabase()
        } catch (e: Exception) {
            DoConsultaDocumento()
        }

}