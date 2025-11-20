package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.model.DoClienteScreen
import javax.inject.Inject

class GetAllSocioNegocioConFacturasUseCase @Inject constructor(
    private val clienteSociosDao: ClienteSociosDao,
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(): List<DoClienteScreen> =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            if (usuario.SuperUser == "Y"){
                clienteSociosDao.getSociosConFacturaPendienteSuperuser()
            } else {
                clienteSociosDao.getSociosConFacturaPendiente()
            }
        } catch (e: Exception) {
            emptyList()
        }
}