package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.GeneralZonasDao
import com.mobile.massiveapp.data.database.dao.UsuarioZonasDao
import javax.inject.Inject

class GetAllUsuarioZonasCreacionUseCase @Inject constructor(
    private val usuarioZonasDao: UsuarioZonasDao,
    private val zonasDao: GeneralZonasDao
) {
    suspend operator fun invoke(userCode: String) =
        try {
            if (userCode.isEmpty()){
                zonasDao.getZonasCreacion()
            } else {
                usuarioZonasDao.getUsuarioZonasCreacion(userCode)
            }
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}