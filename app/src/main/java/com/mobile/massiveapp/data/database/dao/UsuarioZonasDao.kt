package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.UsuarioZonasEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.domain.model.DoUsuarioZonas
import com.mobile.massiveapp.domain.model.updateDataToSend

@Dao
interface UsuarioZonasDao:BaseDao<UsuarioZonasEntity> {

    @Query("SELECT * FROM UsuarioZonas")
    suspend fun getAll(): List<UsuarioZonasEntity>

    @Query("SELECT * FROM UsuarioZonas WHERE Code = :userCode")
    suspend fun getZonas(userCode: String): List<UsuarioZonasEntity>

    @Query("""
        SELECT 
            T1.Code,
            T1.Name
        FROM UsuarioZonas T0
        LEFT JOIN Zona T1 ON T0.CodeZona = T1.Code
        WHERE T0.Code = :usuarioCode
        """)
    suspend fun getUsuarioZonas(usuarioCode: String): List<DoUsuarioZonas>

    @Query("""
        SELECT 
            T0.Code as Code,
            T0.Name as Name, 
            CASE 
            WHEN T0.Code IN (
                            SELECT 
                                CodeZona 
                            FROM UsuarioZonas 
                            WHERE AccLocked = "N" AND Code = :usuarioCode) THEN (1)
            ELSE (0)
        END AS Checked
        FROM Zona T0
        ORDER BY Name
        """)
    suspend fun getUsuarioZonasCreacion(usuarioCode: String): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM UsuarioZonas")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UsuarioZonasEntity>)

    @Transaction
    suspend fun insertZonasToSend(zonasInfo: List<DoNuevoUsuarioItem>, userCode: String, userToCopy:String){
        val zonasCopy = getZonas(userToCopy)
        val zonasToInsert = zonasCopy.map {zona->
            zona.updateDataToSend(
                userCode,
                zonasInfo.filter { it.Code == zona.CodeZona }.first()
            )
        }
        insertAll(zonasToInsert)
    }

    @Query("UPDATE UsuarioZonas SET AccLocked = :accLocked WHERE CodeZona = :code AND Code = :userCode")
    suspend fun updateAccLocked(accLocked: String, code: String, userCode:String)

    @Update
    suspend fun update(items: List<UsuarioZonasEntity>)
}