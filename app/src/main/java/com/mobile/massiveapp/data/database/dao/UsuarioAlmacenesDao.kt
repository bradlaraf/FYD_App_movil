package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.UsuarioAlmacenesEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.domain.model.DoUsuarioAlmacenes
import com.mobile.massiveapp.domain.model.updateDataToSend

@Dao
interface UsuarioAlmacenesDao:BaseDao<UsuarioAlmacenesEntity> {

    @Query("SELECT * FROM UsuarioAlmacenes")
    suspend fun getAll(): List<UsuarioAlmacenesEntity>

    @Query("SELECT * FROM UsuarioAlmacenes WHERE Code = :userCode")
    suspend fun getAlmacenes(userCode: String): List<UsuarioAlmacenesEntity>

    @Query("""
        SELECT 
            T0.WhsCode,
            T1.WhsName
        FROM UsuarioAlmacenes T0
        LEFT JOIN Almacenes T1 ON T0.WhsCode = T1.WhsCode
        WHERE T0.Code = :usuarioCode
    """)
    suspend fun getUsuarioAlmacenes(usuarioCode: String): List<DoUsuarioAlmacenes>

    @Query("""
        SELECT 
            T0.WhsCode as Code,
            T0.WhsName as Name,
            CASE 
            WHEN T0.WhsCode IN (
                            SELECT 
                                WhsCode 
                            FROM UsuarioAlmacenes 
                            WHERE AccLocked = "N" AND Code = :usuarioCode) THEN (1)
            ELSE (0)
        END AS Checked
        FROM Almacenes T0
        """)
    suspend fun getUsuarioAlmacenesCreacion(usuarioCode: String): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM UsuarioAlmacenes")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UsuarioAlmacenesEntity>)

    @Transaction
    suspend fun insertAlmacenesToSend(almacenesInfo: List<DoNuevoUsuarioItem>, userCode: String, userToCopy: String){
        val almacenesCopy = getAlmacenes(userToCopy)
        val almacenesToInsert = almacenesCopy.map { almacen->
            almacen.updateDataToSend(
                userCode,
                almacenesInfo.filter { it.Code == almacen.WhsCode }.first()
            )
        }
        insertAll(almacenesToInsert)
    }

    @Query("UPDATE UsuarioAlmacenes SET AccLocked = :accLocked WHERE WhsCode = :code AND Code = :userCode")
    suspend fun updateAccLocked(accLocked: String, code: String, userCode: String)

    @Update
    suspend fun update(items: List<UsuarioAlmacenesEntity>)
}