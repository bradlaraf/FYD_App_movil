package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.model.GeneralSocioGrupos
import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetAllGeneralGruposSociosUseCase @Inject constructor(
    private val repository: GeneralRepository
){
    suspend operator fun invoke():List<GeneralSocioGrupos> =
        try {
            repository.getAllGeneralGruposSocios()
        } catch (e: Exception) {
            emptyList()
        }

}