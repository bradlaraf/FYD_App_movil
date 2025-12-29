package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.ManifiestoDocumentoDao
import javax.inject.Inject

class GetAllManifiestoDocumentosUseCase @Inject constructor(
    private val manifiestoDocumentoDao: ManifiestoDocumentoDao
) {
    suspend operator fun invoke() =
        try {
            manifiestoDocumentoDao.getAllManDocView()
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}