package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import javax.inject.Inject

class GetAllSocioDireccionesUseCase @Inject constructor(
    private val respository: SocioRepository
){
    suspend operator fun invoke(): List<DoSocioDirecciones>{
        val response = respository.getAllSocioDirecciones()
         return if (response.isNotEmpty()){
            respository.clearSocioDirecciones()
            respository.insertAllSocioDirecciones(response.map { it.toDatabase() })
            return respository.getAllSocioDireccionesFromDatabase()
        } else{
            respository.getAllSocioDireccionesFromDatabase()
        }
    }
}