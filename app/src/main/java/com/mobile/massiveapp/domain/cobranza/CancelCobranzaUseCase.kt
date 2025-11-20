package com.mobile.massiveapp.domain.cobranza

import com.mobile.massiveapp.data.database.dao.ClientePagosDao
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.model.toModel
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.getError
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import java.lang.Exception
import javax.inject.Inject

class CancelCobranzaUseCase @Inject constructor(
    private val cobranzaRepository: CobranzaRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val loginRepository: LoginRepository,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val pagosDao: ClientePagosDao,

    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao
) {
    var mensaje = "Cobranza Cancelada"
    var codigo = 0
    suspend operator fun invoke(accDocEntry: String):DoError =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            pagosDao.updatePagoCanceled("Y", accDocEntry)

            val pagoACancelar = cobranzaRepository.getPagoCabeceraPorAccDocEntry(accDocEntry)
            pagoACancelar.Canceled = "Y"
            pagoACancelar.AccFinalized = "N"

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


            val pagoACancelarModel = pagoACancelar.toModel()

            try {
                datosMaestrosRepository.sendUnDocumento(
                    hashMapOf("ClientePagos" to listOf(pagoACancelarModel as Any)),
                    configuracion,
                    usuario,
                    url
                )
            } catch (e: Exception){
                throw e
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