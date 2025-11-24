package com.mobile.massiveapp.domain.manifiesto

import com.mobile.massiveapp.data.repositories.ManifiestoRepository
import com.mobile.massiveapp.domain.model.DoManifiesto
import javax.inject.Inject

class GetAllManiefiestosUseCase @Inject constructor(
    private val manifiestoRepository: ManifiestoRepository
) {
    suspend operator fun invoke() =
        //listOf(DoManifiesto("34234RFSFS","Brad Lara","VH3434","2025-21-11","Y"))
        manifiestoRepository.getAllManifiestos()

}