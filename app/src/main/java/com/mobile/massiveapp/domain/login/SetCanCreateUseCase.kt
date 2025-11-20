package com.mobile.massiveapp.domain.login

import com.mobile.massiveapp.data.database.dao.UsuarioDao
import javax.inject.Inject

class SetCanCreateUseCase @Inject constructor(
    private val usuarioDao: UsuarioDao
) {
    suspend operator fun invoke() =
        try {
            usuarioDao.changeCanCreate()
            true
        } catch (e: Exception){
            e.printStackTrace()
            throw e
        }
}