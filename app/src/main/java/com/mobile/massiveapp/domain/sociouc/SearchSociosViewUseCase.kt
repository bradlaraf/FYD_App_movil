package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import javax.inject.Inject

class SearchSociosViewUseCase @Inject constructor(
    val sociosDao: ClienteSociosDao
) {
    suspend operator fun invoke(text: String) =
        try {
            sociosDao.searchSociosView(text)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}