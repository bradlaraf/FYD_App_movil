package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.SocioDireccionesDao
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.mobile.massiveapp.domain.model.toDomain
import timber.log.Timber
import javax.inject.Inject

class SocioDireccionesRepository @Inject constructor(
    private val socioDireccionesDao: SocioDireccionesDao
) {

        //Guardar una direccion de despacho
    suspend fun saveDireccion(direccion: DoSocioDirecciones): Boolean {
        return try {
            socioDireccionesDao.saveDireccion(direccion.toDatabase())
            true
        } catch (e: Exception) {
            false
        }
    }

        //Obtener todas las direcciones por CardCode
    suspend fun getAllDireccionesPorCardCode(cardCode: String): List<DoSocioDirecciones> =
        try {
            socioDireccionesDao.getAllDireccionesPorCardCode(cardCode).map { it.toDomain() }
        } catch (e: Exception) {
            Timber.e(e, "Error al obtener todas las direcciones por CardCode")
            emptyList()
        }

        //Obtener el LineNum por CardCode y Tipo
    suspend fun getLineNumPorCardCodeYTipo(cardCode: String, tipo:String): Int {
        return try {
            socioDireccionesDao.getLineNumPorCardCodeYTipo(cardCode, tipo)           //Porque el LineNum se registra desde 0
        } catch (e: Exception) {
            Timber.e(e, "Error al obtener el LineNum por CardCode y Tipo")
            -1
        }
    }

        //Obtener una direccion por cardCode, tipo y LineNum
    suspend fun getDireccionPorCardCodeTipoYLineNum(cardCode: String, tipo: String, lineNum: Int): DoSocioDirecciones {
        return try {
            socioDireccionesDao.getDireccionPorCardCodeTipoYLineNum(cardCode, tipo, lineNum).toDomain()
        } catch (e: Exception) {
            Timber.e(e, "Error al obtener una direccion por cardCode, tipo y LineNum")
            DoSocioDirecciones()
        }
    }

        //Eliminar una direccion por CardCode y tipo
    suspend fun deleteUnaDireccionPorCardCodeYTipo(cardCode: String, tipo: String): Boolean {
        return try {
            socioDireccionesDao.deleteUnaDireccionPorCardCodeYTipo(cardCode, tipo)
            true
        } catch (e: Exception) {
            Timber.e(e, "Error al eliminar una direccion por CardCode y tipo")
            false
        }
    }
}
