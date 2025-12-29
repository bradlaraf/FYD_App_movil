package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.SocioDireccionesEntity
import com.mobile.massiveapp.domain.model.DoDireccion
import com.mobile.massiveapp.domain.model.DoDireccionEdicionView
import com.mobile.massiveapp.domain.model.DoDireccionView
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import kotlinx.coroutines.flow.Flow

@Dao
interface SocioDireccionesDao:BaseDao<SocioDireccionesEntity> {

    @Query("SELECT * FROM SocioDirecciones")
    suspend fun getAllSociosDirecciones(): List<SocioDireccionesEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSocioDirecciones(socioDirecciones: List<SocioDireccionesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocioDirecciones(socioDirecciones: SocioDireccionesEntity)

    @Query("""
        SELECT 
            IFNULL(T1.Name, ' ')        AS Pais,
            IFNULL(T2.Name, ' ')        AS Departamento,
            IFNULL(T0.County, ' ')      AS Provincia,
            IFNULL(T0.City, ' ')        AS Distrito,
            IFNULL(T0.Street, ' ')      AS Calle,
            IFNULL(T0.Block, ' ')       AS Referencia,
            IFNULL(T0.Address, ' ')     AS Direccion, 
            IFNULL(T0.CardCode, ' ')    AS CardCode,
            IFNULL((T0.U_MSV_CP_LATITUD), ' ') AS Latitud,
            IFNULL((T0.U_MSV_CP_LONGITUD), ' ') AS Longitud,
            IFNULL((SELECT Z0.SlpName FROM Vendedor Z0 WHERE SlpCode = T0.U_MSV_MA_VEN), ' ') AS Vendedor,
            IFNULL((SELECT Z0.Name FROM Zona Z0 WHERE Code = T0.U_MSV_MA_ZONA), ' ') AS Zona
        FROM SocioDirecciones T0
        LEFT JOIN Pais T1 ON T0.Country = T1.Code
        LEFT JOIN Departamento T2 ON T0.State = T2.Code
        WHERE T0.CardCode = :cardCode""")
    suspend fun getDireccionesView(cardCode: String): List<DoDireccionView>

    @Query("""
        SELECT 
            IFNULL(T0.Address, '')
        FROM SocioDirecciones T0
        WHERE T0.CardCode = :cardCode
        AND AdresType == "S"
        AND T0.U_MSV_MA_VEN = (SELECT Z0.DefaultSlpCode FROM Usuario Z0 LIMIT 1)
        AND T0.U_MSV_MA_ZONA = (SELECT Z0.DefaultZona FROM Usuario Z0 LIMIT 1)
        
        """)
    suspend fun getDireccionDespachoXZona(cardCode: String): String

    @Query("""
        SELECT 
            IFNULL(T1.Name, ' ')        AS Pais,
            IFNULL(T2.Name, ' ')        AS Departamento,
            IFNULL(T0.County, ' ')      AS Provincia,
            IFNULL(T0.City, ' ')        AS Distrito,
            IFNULL(T0.Street, ' ')      AS Calle,
            IFNULL(T0.Block, ' ')       AS Referencia,
            IFNULL(T0.Address, ' ')     AS Direccion, 
            IFNULL(T0.CardCode, ' ')    AS CardCode,
            IFNULL((T0.U_MSV_CP_LATITUD), ' ') AS Latitud,
            IFNULL((T0.U_MSV_CP_LONGITUD), ' ') AS Longitud,
            IFNULL((SELECT Z0.SlpName FROM Vendedor Z0 WHERE SlpCode = T0.U_MSV_MA_VEN), ' ') AS Vendedor,
            IFNULL((SELECT Z0.Name FROM Zona Z0 WHERE Code = T0.U_MSV_MA_ZONA), ' ') AS Zona,
            
            IFNULL(T1.Code, ' ') AS CodigoPais,
            IFNULL(T2.Code, ' ') AS CodigoDepartamento,
            IFNULL((SELECT Z0.Code FROM Distrito Z0 WHERE Z0.Name = T0.City), ' ') AS CodigoDistrito,
            IFNULL((SELECT Z0.SlpCode FROM Vendedor Z0 WHERE Z0.SlpCode = T0.U_MSV_MA_VEN), -1) AS CodigoVendedor,
            IFNULL((SELECT Z0.Code FROM Zona Z0 WHERE Code = T0.U_MSV_MA_ZONA), ' ') AS CodigoZona
        FROM SocioDirecciones T0
        LEFT JOIN Pais T1 ON T0.Country = T1.Code
        LEFT JOIN Departamento T2 ON T0.State = T2.Code
        WHERE CardCode = :cardCode 
            AND AdresType = :tipo 
            AND LineNum = :lineNum
    """)
    suspend fun getDireccionEdicion(cardCode: String, tipo: String, lineNum: Int): DoDireccionEdicionView

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