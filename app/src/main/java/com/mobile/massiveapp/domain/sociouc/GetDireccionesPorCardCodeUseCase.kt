package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject

class GetDireccionesPorCardCodeUseCase @Inject constructor(
    private val repository: SocioRepository
){
    suspend operator fun invoke(cardCode: String) = repository.getDireccinesPorCardCode(cardCode)
}
