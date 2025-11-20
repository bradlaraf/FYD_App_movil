package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.UsuarioListaPreciosDao
import javax.inject.Inject

class GetAllUsuarioListaPreciosUseCase @Inject constructor(
    private val usuarioListaPreciosDao: UsuarioListaPreciosDao
) {
    suspend operator fun invoke(usuarioCode: String) =
        try {
            usuarioListaPreciosDao.getUsuarioListaPrecios(usuarioCode)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}