package com.mobile.massiveapp.domain.sociouc.direcciones

import com.mobile.massiveapp.data.repositories.SocioDireccionesRepository
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import javax.inject.Inject

class GetAllDireccionesPorCardCodeUseCase @Inject constructor(
    private val direccionesRepository: SocioDireccionesRepository
) {
    suspend operator fun invoke(cardCode: String):List<DoSocioDirecciones> =
        try {
            direccionesRepository.getAllDireccionesPorCardCode(cardCode)
        } catch (e: Exception) {
            emptyList()
        }

}