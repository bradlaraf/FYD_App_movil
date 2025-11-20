package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.BancoDao
import com.mobile.massiveapp.data.database.entities.toModel
import javax.inject.Inject

class GetAllBancosUseCase @Inject constructor(
    private val bancoDao: BancoDao
) {
    suspend operator fun invoke() =
        bancoDao.getAll().map { it.toModel() }
}