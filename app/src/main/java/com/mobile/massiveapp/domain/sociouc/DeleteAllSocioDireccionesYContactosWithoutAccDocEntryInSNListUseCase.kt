package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import javax.inject.Inject

class DeleteAllSocioDireccionesYContactosWithoutAccDocEntryInSNListUseCase @Inject constructor(
    private val sociosDao: ClienteSociosDao
){
    suspend operator fun invoke() =

        try {
            sociosDao.deleteAllDirecSinCabecera()
            sociosDao.deleteAllContacSinCabecera()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
}