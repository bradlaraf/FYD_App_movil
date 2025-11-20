package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloInfo
import timber.log.Timber
import javax.inject.Inject

class GetArticuloInfoUseCase @Inject constructor(
    private val articuloRepository: ArticuloRepository
){
    suspend operator fun invoke (itemCode: String) =
        try {
            articuloRepository.getArticuloInfoConUnidadDeMedida(itemCode)
        } catch (e: Exception) {
            Timber.d(e, "Error en GetArticuloInfoUseCase")
            DoArticuloInfo()
        }
}