package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.domain.model.DoClienteScreen
import javax.inject.Inject

class GetAllSNUseCase @Inject constructor(
    private val clienteSociosDao: ClienteSociosDao
) {
    suspend operator fun invoke(): List<DoClienteScreen> =
        clienteSociosDao.getAll()
}