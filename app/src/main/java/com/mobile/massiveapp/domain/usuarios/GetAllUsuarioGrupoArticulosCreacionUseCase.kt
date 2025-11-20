package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ArticuloGruposDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoArticuloDao
import javax.inject.Inject

class GetAllUsuarioGrupoArticulosCreacionUseCase @Inject constructor(
    private val usuarioGrupoArticuloDao: UsuarioGrupoArticuloDao,
    private val grupoArticulosDao: ArticuloGruposDao
) {
    suspend operator fun invoke(userCode: String) =
        try {
            if (userCode.isEmpty()){
                grupoArticulosDao.getArticuloGruposCreacion()
            }else {
                usuarioGrupoArticuloDao.getUsuarioGrupoArticulosCreacion(userCode)
            }
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}