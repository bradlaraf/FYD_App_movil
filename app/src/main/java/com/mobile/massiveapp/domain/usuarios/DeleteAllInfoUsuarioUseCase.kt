package com.mobile.massiveapp.domain.usuarios

import androidx.sqlite.db.SimpleSQLiteQuery
import com.mobile.massiveapp.data.database.dao.InfoTablasDao
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.DatosMaestrosRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import javax.inject.Inject

class DeleteAllInfoUsuarioUseCase @Inject constructor(
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
        "Configuracion",
        "ClientePagos",
        "ClientePedidos",
        "ClientePagosDetalle",
        "ClientePedidosDetalle",
        "UsuariosAlmacenes",
        "UsuariosZonas",
        "UsuariosListaPrecios",
        "UsuariosGruposSocios",
        "UsuariosGrupoArticulos",
        "Usuarios"
    )
    suspend operator fun invoke(): Boolean =
        try {
            val configuracion = configuracionRepository.getConfiguracion()

            val usuario = usuarioRepository.getUsuarioFromDatabase()
            val url = getUrlFromConfiguracion(configuracion)


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