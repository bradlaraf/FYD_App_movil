package com.mobile.massiveapp.domain

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.database.dao.InfoTablasDao
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.CobranzaRepository
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.PedidoRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.model.DoError
import java.lang.Exception
import javax.inject.Inject

class SendDatosMaestrosUseCase @Inject constructor(
    private val repository: DatosMaestrosRepository,
    private val pedidosRepository: PedidoRepository,
    private val cobranzaRepository: CobranzaRepository,
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val infoTableDao: InfoTablasDao,

    private val datosMaestrosService: DatosMaestrosService,
    private val errorLogDao: ErrorLogDao,

    private val loginRepository: LoginRepository
){
    var mensaje = "Documentos sincronizados"
    var codigo = 0
    suspend operator fun invoke(progressCallBack: (Int, String, Int) -> Unit): DoError {
        return try {
            val configuracion = configuracionRepository.getConfiguracion()
            val usuario = loginRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)

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

            deleteAllEditableData()

            if (repository.sendDatosMaestros(progressCallBack, configuracion, usuario, url) ) {
                datosMaestrosRepository.getDatosMaestrosFromEndpointAndSave(listOf(
                    "ClientePagos",
                    "ClientePedidos",
                    ManagerInputData.FACTURAS_CL,
                    ManagerInputData.FACTURAS_CL_DETALLE),
                    configuracion,
                    usuario,
                    url
                ){progress, message, maxLenght->
                }
            }


            DoError(
                mensaje,
                codigo
            )
        } catch (e:Exception) {
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }
    }

    private suspend fun deleteAllEditableData() {
        val pediddosDetalleEditados = pedidosRepository.getAllPedidoDetalle().filter { it.LineNum >= 1000 }
        val pagosDetalleEditados = cobranzaRepository.getAllPagosDetalle().filter { it.DocLine >= 1000 }


        val listSuccessDelete = mutableListOf<Boolean>()
        pediddosDetalleEditados.forEach {
            listSuccessDelete.add(pedidosRepository.deleteUnPedidoDetallePorAccDocEntryYLineNum(it.AccDocEntry, it.LineNum))
        }

        val listSuccessDeletePagos = mutableListOf<Boolean>()
        pagosDetalleEditados.forEach {
            listSuccessDeletePagos.add(cobranzaRepository.deleteUnPagoDetallePorAccDocEntryYDocLine(it.AccDocEntry, it.DocLine))
        }
    }
}