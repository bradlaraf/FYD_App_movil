package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.FormaPagoDao
import com.mobile.massiveapp.data.model.toModel
import javax.inject.Inject

class GetAllFormasPagoUseCase @Inject constructor(
    private val formaPagoDao: FormaPagoDao
) {
    suspend operator fun invoke() =
        try {
            formaPagoDao.getAll().map { it.toModel() }
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
}