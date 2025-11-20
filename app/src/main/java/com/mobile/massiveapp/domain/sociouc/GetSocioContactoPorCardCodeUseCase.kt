package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import javax.inject.Inject

class GetSocioContactoPorCardCodeUseCase @Inject constructor(
    private val respository: SocioRepository
) {
    suspend operator fun invoke(cardCode: String) = respository.getSocioContactoPorCardCode(cardCode)
}