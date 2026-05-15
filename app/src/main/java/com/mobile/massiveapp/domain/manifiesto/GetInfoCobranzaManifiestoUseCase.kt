package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.database.dao.ManifiestoDao
import com.mobile.massiveapp.data.database.dao.ManifiestoDocumentoDao
import javax.inject.Inject

class GetInfoCobranzaManifiestoUseCase @Inject constructor (
    val manifiestoDocumentoDao: ManifiestoDocumentoDao
) {
    suspend operator fun invoke(docEntry: Int) =

            manifiestoDocumentoDao.GetInfoCobranzaManifiesto(docEntry)

}