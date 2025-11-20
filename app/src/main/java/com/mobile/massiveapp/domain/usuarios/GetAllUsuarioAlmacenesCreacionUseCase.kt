package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ArticuloAlmacenesDao
import com.mobile.massiveapp.data.database.dao.UsuarioAlmacenesDao
import javax.inject.Inject

class GetAllUsuarioAlmacenesCreacionUseCase @Inject constructor(
    private val usuarioAlmacenesDao: UsuarioAlmacenesDao,
    private val almacenesDao: ArticuloAlmacenesDao
) {
    suspend operator fun invoke(userCode: String) =
        try {
            if (userCode.isEmpty()){
                almacenesDao.getAlmacenesCreacion()
            } else {
                usuarioAlmacenesDao.getUsuarioAlmacenesCreacion(userCode)
            }
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}