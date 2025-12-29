package com.mobile.massiveapp.domain.usuarios

import com.mobile.massiveapp.data.database.dao.ConfigurarUsuarioDao
import javax.inject.Inject

class GetUsuarioInfoUseCase @Inject constructor(
    private val configuracionUsuarioDao: ConfigurarUsuarioDao
) {
    suspend operator fun invoke(code: String) =
        try {
            configuracionUsuarioDao.getUsuarioViewInfo(code)
        } catch (e:Exception) {
            e.printStackTrace()
            null
        }
}