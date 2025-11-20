package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.GeneralSocioGruposDao
import com.mobile.massiveapp.data.database.dao.UsuarioGrupoSociosDao
import javax.inject.Inject

class GetAllUsuarioGrupoSociosCreacionUseCase @Inject constructor(
    private val usuarioGrupoSociosDao: UsuarioGrupoSociosDao,
    private val grupoSociosDao: GeneralSocioGruposDao
) {
    suspend operator fun invoke(userCode: String) =
        try {
            if (userCode.isEmpty()){
                grupoSociosDao.getSocioGruposCreacion()
            }else{
                usuarioGrupoSociosDao.getUsuarioGrupoSociosCreacion(userCode)
            }
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}