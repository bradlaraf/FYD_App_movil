package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsuariosUseCase @Inject constructor(
    private val configurarUsuarioDao: ConfigurarUsuarioDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke() =
        try {
            configurarUsuarioDao.getAll().map { it.toDomain() }
        } catch (e:Exception){
            errorLogDao.insert(getError("$e","${e.message}"))
            emptyList()
        }

    fun getAllUsuariosFlow(): Flow<List<DoConfigurarUsuario>> = configurarUsuarioDao.getAllCUsuariosFlow()
}