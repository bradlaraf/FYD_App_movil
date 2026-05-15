package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.database.dao.ManifiestoDao
import com.mobile.massiveapp.data.repositories.ManifiestoRepository
import com.mobile.massiveapp.domain.model.DoManifiesto
import javax.inject.Inject

class GetAllManiefiestosUseCase @Inject constructor(
    private val manifiestoDao: ManifiestoDao
) {
    suspend operator fun invoke() =
        manifiestoDao.getAllManifiestosView()

}