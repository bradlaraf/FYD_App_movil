package com.mobile.massiveapp.domain

import androidx.sqlite.db.SimpleSQLiteQuery
import com.mobile.massiveapp.data.database.dao.InfoTablasDao
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import javax.inject.Inject

class SendReinicializacionDeMaestrosUseCase @Inject constructor(
    private val datosMaestrosRepository: DatosMaestrosRepository,
    private val usuarioRepository: LoginRepository,
    private val configuracionRepository: ConfiguracionRepository,
    private val infoTableDao: InfoTablasDao
) {
    private val tablasNoMostrar = listOf(
        "InfoTablas",
        "android_metadata",
        "room_master_table",
        "ConsultaDocumentoContactos",
        "ConsultaDocumentoDirecciones",
        "SocioDniConsulta",
        "SocioNuevoAwait",
        "ConsultaDocumento",
        "Usuario",
        "Bases",
        "Configuracion"
        )

    suspend operator fun invoke(): Boolean =
        try {
            val configuracion = configuracionRepository.getConfiguracion()

            val usuario = usuarioRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)

            /*val deleteAllMaestros = datosMaestrosRepository.deleteAllDatosMaestros()

            if (deleteAllMaestros){
                datosMaestrosRepository.sendReinicializacion(configuracion, usuario, url)
            }*/

            val queryListTables = SimpleSQLiteQuery("SELECT name FROM sqlite_master WHERE type='table'")
            val listaTablas = infoTableDao.getAllTableNames(queryListTables).filter { it !in tablasNoMostrar }

            infoTableDao.deleteAllTables(listaTablas)
            datosMaestrosRepository.sendReinicializacion(configuracion, usuario, url)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}