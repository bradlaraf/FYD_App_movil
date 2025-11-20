package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDetalleDao
import javax.inject.Inject

class GetAllPagoDetalleXAccDocEntryUseCase @Inject constructor(
    val pagosDetalleDao: ClientePagosDetalleDao
) {
    suspend operator fun invoke(accDocEntry:String) =
        try {
            pagosDetalleDao.getAllPagoDetalleXAccDocEntry(accDocEntry)
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }

}