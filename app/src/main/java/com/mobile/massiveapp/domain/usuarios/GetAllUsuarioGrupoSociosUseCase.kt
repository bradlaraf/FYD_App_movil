package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.UsuarioGrupoSociosDao
import javax.inject.Inject

class GetAllUsuarioGrupoSociosUseCase @Inject constructor(
    private val usuarioGrupoSociosDao: UsuarioGrupoSociosDao
) {
    suspend operator fun invoke(usuarioCode: String) =
        try {
            usuarioGrupoSociosDao.getUsuarioGrupoSocios(usuarioCode)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}