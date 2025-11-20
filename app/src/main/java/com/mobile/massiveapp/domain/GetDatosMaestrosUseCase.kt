package com.mobile.massiveapp.domain

import androidx.sqlite.db.SimpleSQLiteQuery
import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.network.DatosMaestrosService
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.util.ManagerInputData
import com.mobile.massiveapp.domain.model.DoError
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mobile.massiveapp.data.database.dao.InfoTablasDao
import javax.inject.Inject

class GetDatosMaestrosUseCase @Inject constructor(
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val loginRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val datosMaestrosService: DatosMaestrosService,
    private val infoTableDao: InfoTablasDao,

    private val errorLogDao: ErrorLogDao

) {
    var mensaje = "Datos Maestros sincronizados"
    var codigo = 0
    suspend operator fun invoke(progressCallBack: (Int, String, Int) -> Unit): DoError =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            if (usuario.Code.isNotEmpty() && configuracion.IpPublica.isNotEmpty()) {

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

                val listaPendientes = datosMaestrosService.getPendientes(
                    configuracion,
                    usuario,
                    url,
                    20L
                ).filter { it.Cantidad > 0 }

                val listaDatosBetados = listOf(
                    ManagerInputData.CLIENTE_SOCIOS,
                    ManagerInputData.CLIENTE_SOCIOS_CONTACTOS,
                    ManagerInputData.CLIENTE_SOCIOS_DIRECCIONES,
                    ManagerInputData.ARTICULO,
                    ManagerInputData.ARTICULO_CANTIDADES,
                    ManagerInputData.ARTICULO_PRECIOS,
                    ManagerInputData.FACTURAS_CL,
                    ManagerInputData.FACTURAS_CL_DETALLE)


                val listaFiltrada = listaPendientes.filter { it.Metodo !in listaDatosBetados }
                val listaPendientesAObtener = listaFiltrada.map { it.Metodo }

                borrarTablas()
                datosMaestrosRepository.getDatosMaestrosFromEndpointAndSave(
                    listaPendientesAObtener,
                    configuracion,
                    usuario,
                    url,
                    0
                ){ progress, message, maxLenght->
                    progressCallBack(progress, message, maxLenght)
                }


            }
            DoError(
                mensaje,
                codigo
            )
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            e.printStackTrace()
            DoError(ErrorMensaje = e.message.toString(), ErrorCodigo = codigo)
        }

    private suspend fun borrarTablas(){
        val queryListTables = SimpleSQLiteQuery("SELECT name FROM sqlite_master WHERE type='table'")
        val listaTablas = infoTableDao.getAllTableNames(queryListTables).filter { it !in ManagerInputData.tablasNoMostrar }

        infoTableDao.deleteAllTables(listaTablas)
    }
}