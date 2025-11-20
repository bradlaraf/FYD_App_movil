package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import javax.inject.Inject

class GetArticuloPorItemCodeUseCase @Inject constructor(
    private val repository: ArticuloRepository
){
    suspend operator fun invoke(ItemCode: String) = repository.getArticuloPorItemCode(ItemCode)
}