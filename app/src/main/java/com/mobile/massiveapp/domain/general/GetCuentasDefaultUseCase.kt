package com.mobile.massiveapp.domain.general

import com.mobile.massiveapp.data.database.dao.GeneralCuentasBDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCuentasDefaultUseCase @Inject constructor(
    private val generalCuentasBDao: GeneralCuentasBDao
) {
    fun getCuentaTransferenciaDetault(): Flow<String> = generalCuentasBDao.getCuentaTransferenciaDefault()
    fun getCuentaCashDefault(): Flow<String> = generalCuentasBDao.getCuentaCashDefault()
    fun getCuentaChequeDefault(): Flow<String> = generalCuentasBDao.getCuentaChequeDefault()
    fun getCuentaDepositoDefault(): Flow<String> = generalCuentasBDao.getCuentaDepositoDefault()
}