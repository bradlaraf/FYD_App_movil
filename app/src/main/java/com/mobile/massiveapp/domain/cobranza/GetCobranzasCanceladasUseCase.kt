package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.model.toDomain
import com.mobile.massiveapp.ui.view.util.getFechaActual
import javax.inject.Inject

class GetCobranzasCanceladasUseCase @Inject constructor(
    private val pagosDao: ClientePagosDao,
    private val errorLogDao: ErrorLogDao
) {
    suspend operator fun invoke() =
        try {
            pagosDao.getPagosCancelados(getFechaActual()).map { it.toDomain() }
        } catch (e:Exception){
            errorLogDao.insert(
                getError("${e.message}", "${e.message}")
            )
            emptyList()
        }
}