package com.mobile.massiveapp.domain.facturas

import com.mobile.massiveapp.data.database.dao.ClienteFacturasDao
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.model.DoClienteFacturas
import com.mobile.massiveapp.domain.model.toDomain
import javax.inject.Inject

class GetAllFacturasPorCardCodeUseCase @Inject constructor(
    private val facturasDao: ClienteFacturasDao,
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(cardCode: String):List<DoClienteFacturas> =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            if (usuario.SuperUser == "Y"){
                facturasDao.getAllFacturasPorCardCodeSuperUser(cardCode)
            } else{
                facturasDao.getAllFacturasPorCardCode(cardCode)
            }.map { it.toDomain() }

        } catch (e: Exception){
            emptyList()
        }
}