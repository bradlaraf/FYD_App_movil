package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import javax.inject.Inject

class GetAllDireccionPorTipoYCardCodeUseCase @Inject constructor(
    private val repository: SocioRepository
){
    suspend operator fun invoke(cardCode: String, tipo:String): List<DoSocioDirecciones> =
        try {
            repository.getDireccionesPorTipoYCardCode(cardCode, tipo)
        } catch (e: Exception) {
            emptyList()
        }

}