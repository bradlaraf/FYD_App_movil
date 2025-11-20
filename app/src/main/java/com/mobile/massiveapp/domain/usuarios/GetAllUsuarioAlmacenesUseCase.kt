package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.UsuarioAlmacenesDao
import javax.inject.Inject

class GetAllUsuarioAlmacenesUseCase @Inject constructor(
    private val usuarioAlmacenesDao: UsuarioAlmacenesDao
) {
    suspend operator fun invoke(usuarioCode: String) =
        try {
            usuarioAlmacenesDao.getUsuarioAlmacenes(usuarioCode)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}