package com.mobile.massiveapp.domain.facturas

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import javax.inject.Inject

class SetEditPtdToDefaultUseCase @Inject constructor(
    val clienteFacturasDao: ClienteFacturasDao
) {
    suspend operator fun invoke() =
        try {
            clienteFacturasDao.setEditPtdToDeafult()
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}