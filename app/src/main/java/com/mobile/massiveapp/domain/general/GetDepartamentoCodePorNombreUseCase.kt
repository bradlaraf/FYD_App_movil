package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.repositories.GeneralRepository
import javax.inject.Inject

class GetDepartamentoCodePorNombreUseCase @Inject constructor(
    private val repository: GeneralRepository
) {
    suspend operator fun invoke(nombre:String):String =
        try{
            repository.getDepartamentoCodePorNombre(nombre)
        } catch (e: Exception){
            ""
        }
}