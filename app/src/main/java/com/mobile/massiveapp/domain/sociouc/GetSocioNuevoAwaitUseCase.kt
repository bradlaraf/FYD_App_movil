package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoSocioNuevoAwait
import javax.inject.Inject

class GetSocioNuevoAwaitUseCase @Inject constructor(
    private val repository: SocioRepository
) {
    suspend operator fun invoke():List<DoSocioNuevoAwait> =
        try {
            repository.getSocioNuevoAwaitFromDatabase()
        } catch (e: Exception) {
            emptyList()
        }

}
