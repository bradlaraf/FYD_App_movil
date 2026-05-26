package com.mobile.massiveapp.domain.rutacomercial

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.RutaComercialDao
import com.mobile.massiveapp.data.database.dao.RutaComercialDetalleDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.model.RutaComercial
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import javax.inject.Inject

class SaveRutaComercialUseCase @Inject constructor(
    private val rutaDao: RutaComercialDao,
    private val rutaDetalleDao: RutaComercialDetalleDao,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,

    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao
) {
    var mensaje = "Ruta enviada"
    var codigo = 0
    suspend operator fun invoke(rutaComercialCabecera: RutaComercial) =

        try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)

            rutaDao.insert(rutaComercialCabecera.toDatabase())

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
                }
            }

            val detalle = rutaDetalleDao.getAllByCode(rutaComercialCabecera.AccDocEntry).map { it.toModel() }
            rutaComercialCabecera.Lineas = detalle

            val hashMapToSend = hashMapOf("ClienteRutasComerciales" to listOf(rutaComercialCabecera as Any))

            val sendPedidoToWebService =
                datosMaestrosRepository.sendUnDocumento(
                    hashMapToSend,
                    configuracion,
                    usuario,
                    url,
                    11L
                )

            if (sendPedidoToWebService){
                rutaDao.insert(rutaComercialCabecera.toDatabase())
                datosMaestrosRepository.getDatosMaestrosFromEndpointAndSave(listOf(
                    "ClienteRutasComerciales"),
                    configuracion,
                    usuario,
                    url,
                    600,
                    5L
                ){progress, message, maxLenght->}
            } else {
                rutaDao.insert(rutaComercialCabecera.toDatabase())
            }

            DoError(
                mensaje,
                codigo
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }

}
