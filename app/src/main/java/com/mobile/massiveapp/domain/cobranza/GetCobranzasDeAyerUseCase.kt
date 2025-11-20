package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.model.toDomain
import com.mobile.massiveapp.ui.view.util.getFechaAyer
import javax.inject.Inject

class GetCobranzasDeAyerUseCase @Inject constructor(
    private val cobranzasDao: ClientePagosDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke() =
        try {
            cobranzasDao.getAll(getFechaAyer()).map { it.toDomain() }
        } catch (e: Exception){
            errorLogDao.insert(getError("${e.message}","${e.message}"))
            emptyList()
        }
}