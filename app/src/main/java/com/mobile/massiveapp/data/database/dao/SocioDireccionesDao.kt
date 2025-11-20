package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.SocioDireccionesEntity
import com.mobile.massiveapp.domain.model.DoDireccion
import kotlinx.coroutines.flow.Flow

@Dao
interface SocioDireccionesDao:BaseDao<SocioDireccionesEntity> {

    @Query("SELECT * FROM SocioDirecciones")
    suspend fun getAllSociosDirecciones(): List<SocioDireccionesEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSocioDirecciones(socioDirecciones: List<SocioDireccionesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocioDirecciones(socioDirecciones: SocioDireccionesEntity)


    @Update
    suspend fun updateSocioDirecciones(socioDirecciones: List<SocioDireccionesEntity>)


    @Query("DELETE FROM SocioDirecciones")
    suspend fun clearAllSociosDirecciones()


    //Eliminar todas las direcciones de despacho por AccDocEntry
    @Query("DELETE FROM SocioDirecciones WHERE AccDocEntry = :accDocEntry")
    suspend fun deleteSocioDireccionesByAccDocEntry(accDocEntry: String)


    //Buscar direcciones por CardCode
    @Query("SELECT * FROM SocioDirecciones WHERE CardCode = :cardCode")
    suspend fun getDireccionPorCardCode(cardCode: String): List<SocioDireccionesEntity>

    //Buscar direcciones de despacho por CardCode y tipo
    @Query("""
            SELECT 
            T0.AdresType,
            T0.Street
            FROM SocioDirecciones T0 
            WHERE T0.CardCode = :cardCode""")
    fun getDirecciones(cardCode: String): Flow<List<DoDireccion>>

    //Buscar direcciones de despacho por CardCode
    @Query("SELECT * FROM SocioDirecciones WHERE CardCode = :cardCode")
    suspend fun getDireccionesPorCardCode(cardCode: String): List<SocioDireccionesEntity>



    @Query("SELECT * FROM SocioDirecciones WHERE CardCode = :cardCode AND AdresType = :tipo")
    suspend fun getDireccionesPorTipoYCardCode(cardCode: String, tipo: String): List<SocioDireccionesEntity>



        //Eliminar direccion de socio de negocio por CardCode
    @Query("DELETE FROM SocioDirecciones WHERE CardCode = :cardCode ")
    suspend fun deleteSocioDireccionesByCardCode(cardCode: String)



        //Guardar una direccion por CardCode, Tipo y LineNum
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDireccion(socioDirecciones: SocioDireccionesEntity)


        //Obtener todas las direcciones por CardCode
    @Query("SELECT * FROM SocioDirecciones WHERE CardCode = :cardCode ORDER BY AdresType")
    suspend fun getAllDireccionesPorCardCode(cardCode: String): List<SocioDireccionesEntity>


        //Obtener LineNum Por CardCode y Tipo de direccion
    @Query("SELECT COUNT(T0.Address) FROM SocioDirecciones T0 WHERE T0.CardCode = :cardCode AND T0.AdresType = :tipo")
    suspend fun getLineNumPorCardCodeYTipo(cardCode: String, tipo: String): Int

        //Obtener una direccion por cardCode, tipo y LineNum
    @Query("SELECT * FROM SocioDirecciones WHERE CardCode = :cardCode AND AdresType = :tipo AND LineNum = :lineNum")
    suspend fun getDireccionPorCardCodeTipoYLineNum(cardCode: String, tipo: String, lineNum: Int): SocioDireccionesEntity

        //Eliminar una direccion por CardCode y tipo
    @Query("DELETE FROM SocioDirecciones WHERE CardCode = :cardCode AND AdresType = :tipo")
    suspend fun deleteUnaDireccionPorCardCodeYTipo(cardCode: String, tipo: String)
}