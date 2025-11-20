package com.mobile.massiveapp.domain.sociouc

import com.mobile.massiveapp.data.database.dao.ClienteSociosDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.SocioContactosDao
import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.ClienteSocios
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.SocioRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoClienteSocios
import com.mobile.massiveapp.domain.model.DoError
import javax.inject.Inject

class InsertarSocioNuevoAwaitUseCase @Inject constructor(
    private val socioRespository: SocioRepository,
    private val sociosDao: ClienteSociosDao,
    private val socioDireccionesDao: SocioDireccionesDao,
    private val socioContactosDao: SocioContactosDao,
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao
){
    var mensaje = "Socio Registrado"
    var codigo = 0
    suspend operator fun invoke(clienteSocio: DoClienteSocios):DoError =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)
            val endpoint = "ClienteSocios"

            socioRespository.insertSocio(clienteSocio.toDatabase())

            val estadoSesion = datosMaestrosService.getEstadoSesion(
                usuario,
                configuracion,
                url,
                10L
            )

            when(estadoSesion){
                is String ->{
                    if (estadoSesion == "N"){
                        mensaje = "Su sesión esta cerrada"
                        codigo = ManagerInputData.SESION_CERRADA
                        throw Exception(mensaje)
                    }
                }
                is DoError ->{
                    mensaje = estadoSesion.ErrorMensaje
                    codigo = estadoSesion.ErrorCodigo
                    errorLogDao.insert(getError(codigo.toString(), mensaje))
                    throw Exception(mensaje)
                }
            }


            val contactos = socioRespository.getSocioContactoPorCardCode(clienteSocio.CardCode).map { it.toModel() }
            val direcciones = socioRespository.getDireccinesPorCardCode(clienteSocio.CardCode).map { it.toModel() }

            val clienteSocioModelToSend = clienteSocio.toModel(contactos, direcciones)

            val hashMapToSend = hashMapOf(endpoint to listOf(clienteSocioModelToSend as Any))

            val sendSocioToWebService =
                try {
                    datosMaestrosService.sendDatosMaestrosToEndpointWithRBody(
                        hashMapToSend,
                        configuracion,
                        usuario,
                        url,
                        11L
                    ) as List<ClienteSocios>
                } catch (e:Exception){
                    e.printStackTrace()
                    emptyList()
                }
            if (sendSocioToWebService.isNotEmpty()){
                sociosDao.insertAllSocios(sendSocioToWebService.map { it.toDatabase() })

                sendSocioToWebService.forEach { socio->
                    socioContactosDao.insertAllSociosContactos(socio.Contactos.map { it.toDatabase() })
                    socioDireccionesDao.insertAllSocioDirecciones(socio.Direcciones.map { it.toDatabase() })

                    if (socio.AccError.isNotEmpty()){
                        errorLogDao.insert(
                            getError(
                                code = "Insertar $endpoint - ${socio.CardCode}",
                                message = socio.AccError
                            )
                        )
                    }
                }
            }

            DoError(
                mensaje,
                codigo
            )
        } catch (e: Exception){
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }

}