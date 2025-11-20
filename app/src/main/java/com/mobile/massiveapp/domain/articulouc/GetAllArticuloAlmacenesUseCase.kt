package com.mobile.massiveapp.domain.articulouc

import com.mobile.massiveapp.data.repositories.ArticuloRepository
import com.mobile.massiveapp.domain.model.DoArticuloAlmacenes
import javax.inject.Inject

class GetAllArticuloAlmacenesUseCase @Inject constructor(
    private val repository: ArticuloRepository
) {
    suspend operator fun invoke():List<DoArticuloAlmacenes> {
        return try {
            repository.getAllArticuloAlmacenesFromDatabase()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
