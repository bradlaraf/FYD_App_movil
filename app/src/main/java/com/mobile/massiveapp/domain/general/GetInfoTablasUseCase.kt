package com.mobile.massiveapp.domain.general

import androidx.sqlite.db.SimpleSQLiteQuery
import com.mobile.massiveapp.data.database.dao.InfoTablasDao
import com.mobile.massiveapp.data.database.dao.UsuarioDao
import com.mobile.massiveapp.data.model.InfoTable
import com.mobile.massiveapp.data.util.DefaultValues
import javax.inject.Inject

class GetInfoTablasUseCase @Inject constructor(
    private val infoTableDao: InfoTablasDao,
    private val usuarioDao: UsuarioDao
) {
    private val tablasNoMostrar = listOf(
        "InfoTablas",
        "room_master_table",
        "ConsultaDocumentoContactos",
        "ConsultaDocumentoDirecciones",
        "SocioDniConsulta",
        "SocioNuevoAwait",
        "ConsultaDocumento",
        "Configuracion",
        "SocioGrupos",
        "Bases",
        "ErrorLog",
        "android_metadata")

    suspend operator fun invoke() =
        try {
            val listaInfoTablas: MutableList<InfoTable> = mutableListOf()


            val queryListTables = SimpleSQLiteQuery("SELECT name FROM sqlite_master WHERE type='table'")
            var listaTablas = infoTableDao.getAllTableNames(queryListTables).filter { it !in tablasNoMostrar }

            if (usuarioDao.getAll().SuperUser == "N"){
                listaTablas = listaTablas.filter { it !in DefaultValues.listaInfoUsuario }
            }

            listaTablas.forEach { tabla->
                val queryCount = SimpleSQLiteQuery("SELECT COUNT(*) FROM $tabla")
                val cantidadRegistro = infoTableDao.getCount(queryCount)
                listaInfoTablas.add(InfoTable(tabla, cantidadRegistro))
            }
            listaInfoTablas.sortBy { it.Tabla }
            listaInfoTablas.toList()

        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}