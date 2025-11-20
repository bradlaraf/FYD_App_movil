package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.UsuarioGrupoArticuloDao
import javax.inject.Inject

class GetAllUsuarioGrupoArticulosUseCase @Inject constructor(
    private val usuarioGrupoArticulosDao: UsuarioGrupoArticuloDao
) {
    suspend operator fun invoke(usuarioCode: String) =
        try {
            usuarioGrupoArticulosDao.getUsuarioGrupoArticulos(usuarioCode)
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}