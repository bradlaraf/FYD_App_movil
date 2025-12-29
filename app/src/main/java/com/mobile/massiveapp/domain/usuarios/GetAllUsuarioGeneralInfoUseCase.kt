package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import com.mobile.massiveapp.domain.model.DoConfigurarUsuarioInfo
import javax.inject.Inject

class GetAllUsuarioGeneralInfoUseCase @Inject constructor(
    private val configurarUsuarioDao: ConfigurarUsuarioDao
) {
    suspend operator fun invoke(userCode: String) =
        try {
            if (userCode.isEmpty()){
                throw Exception("Codigo Vacío")
            }
            configurarUsuarioDao.getUsuarioInfoCodes(userCode)
        } catch (e:Exception){
            e.printStackTrace()
            DoConfigurarUsuarioInfo()
        }
}