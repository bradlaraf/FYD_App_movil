package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloPrecioYNombreLista
import javax.inject.Inject

class GetArticuloPrecioListaNombreUseCase @Inject constructor(
    private val repository: ArticuloRepository
) {
    suspend operator fun invoke(): List<DoArticuloPrecioYNombreLista>{
        return repository.getArticuloPreciosYNombreLista()
    }

}