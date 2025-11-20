package com.mobile.massiveapp.domain.sociouc.sociocontacto

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.SocioContactosDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.ClienteSocios
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import javax.inject.Inject

class SendAllLeadsToWServiceUseCase  @Inject constructor(
    private val sociosDao: ClienteSociosDao,
    private val socioDireccionesDao: SocioDireccionesDao,
    private val socioContactosDao: SocioContactosDao,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val datosMaestrosService: DatosMaestrosService,
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository
){
    suspend operator fun invoke() =
        try {

            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            sociosDao.getAllSociosNoMigrados().map {
                val direcciones = socioDireccionesDao.getDireccionPorCardCode(it.CardCode)
                val contactos = socioContactosDao.getContactosPorCardCode(it.CardCode)

                val leadToSendd = it.toModel(
                    direcciones = direcciones,
                    contactos = contactos
                )
                val hashMapToSend = hashMapOf("ClienteSocios" to listOf(leadToSendd as Any))
                /*datosMaestrosRepository.sendUnDocumento(hashMapToSend, configuracion, usuario, url)*/
                val sendSocioToWebService =
                    try {
                        datosMaestrosService.sendDatosMaestrosToEndpointWithRBody(
                            hashMapToSend,
                            configuracion,
                            usuario,
                            url,
                            5L
                        ) as List<ClienteSocios>
                    } catch (e:Exception){
                        e.printStackTrace()
                        emptyList()
                    }
                if (sendSocioToWebService.isNotEmpty()){
                    sociosDao.insertAllSocios(sendSocioToWebService.map { it.toDatabase() })
                }
            }

            true

        } catch (e:Exception){
            e.printStackTrace()
            false
        }

}