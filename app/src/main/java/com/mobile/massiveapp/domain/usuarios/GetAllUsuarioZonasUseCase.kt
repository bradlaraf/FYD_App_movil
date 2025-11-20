package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.UsuarioZonasDao
import javax.inject.Inject

class GetAllUsuarioZonasUseCase @Inject constructor(
    private val usuarioZonasDao: UsuarioZonasDao
) {
    suspend operator fun invoke(usuarioCode: String) =
        try {
            usuarioZonasDao.getUsuarioZonas(usuarioCode)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}