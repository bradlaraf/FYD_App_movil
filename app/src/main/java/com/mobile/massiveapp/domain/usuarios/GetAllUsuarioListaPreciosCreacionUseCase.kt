package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ArticuloListaPreciosDao
import com.mobile.massiveapp.data.database.dao.UsuarioListaPreciosDao
import javax.inject.Inject

class GetAllUsuarioListaPreciosCreacionUseCase @Inject constructor(
    private val usuarioListaPreciosDao: UsuarioListaPreciosDao,
    private val listaPreciosDao: ArticuloListaPreciosDao
) {
    suspend operator fun invoke(userCode: String) =
        try {
            if (userCode.isEmpty()){
                listaPreciosDao.getListasPrecioCreacion()
            } else {
                usuarioListaPreciosDao.getUsuarioListaPreciosCreacion(userCode)
            }
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}