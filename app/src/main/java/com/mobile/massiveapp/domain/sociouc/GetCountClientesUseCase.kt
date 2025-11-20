package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountClientesUseCase @Inject constructor(
    val sociosDao: ClienteSociosDao
) {
    fun getCountClientesFlow():Flow<Int> =
        sociosDao.getCountClientes()
}