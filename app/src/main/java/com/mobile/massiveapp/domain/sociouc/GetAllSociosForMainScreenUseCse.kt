package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import javax.inject.Inject

class GetAllSociosForMainScreenUseCse @Inject constructor(
    val sociosDao: ClienteSociosDao
) {
    suspend operator fun invoke() =
        try {
            sociosDao.getAllSociosScreen()
        } catch (e:Exception){
            emptyList()
        }
}