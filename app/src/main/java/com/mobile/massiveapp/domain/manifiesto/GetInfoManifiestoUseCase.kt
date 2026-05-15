package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.ManifiestoDao
import com.mobile.massiveapp.domain.model.DoManifiestoView
import javax.inject.Inject

class GetInfoManifiestoUseCase @Inject constructor(
    val manifiestoDao: ManifiestoDao
) {
    suspend operator fun invoke(docEntry: Int) =
        try {
            manifiestoDao.getManifiestosView(docEntry)
        } catch (e:Exception){
            e.printStackTrace()
            DoManifiestoView()
        }
}