package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.UsuarioListaPreciosEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.domain.model.DoUsuarioListaPrecios
import com.mobile.massiveapp.domain.model.updateDataToSend

@Dao
interface UsuarioListaPreciosDao:BaseDao<UsuarioListaPreciosEntity> {

    @Query("SELECT * FROM UsuarioListaPrecios")
    suspend fun getAll(): List<UsuarioListaPreciosEntity>

    @Query("SELECT * FROM UsuarioListaPrecios WHERE Code = :userCode")
    suspend fun getListasPrecio(userCode: String): List<UsuarioListaPreciosEntity>

    @Query("""
        SELECT 
             T0.ListNum,
             T1.ListName
        FROM UsuarioListaPrecios T0
        LEFT JOIN ListaPrecio T1 ON T1.ListNum = T0.ListNum
        WHERE T0.Code = :usuarioCode
        """)
    suspend fun getUsuarioListaPrecios(usuarioCode: String): List<DoUsuarioListaPrecios>


    @Query("""
        SELECT 
            T0.ListNum as Code,
            T0.ListName as Name,
            CASE 
            WHEN T0.ListNum IN (
                            SELECT 
                                ListNum 
                            FROM UsuarioListaPrecios 
                            WHERE AccLocked = "N" AND Code = :usuarioCode) THEN (1)
            ELSE (0)
        END AS Checked
        FROM ListaPrecio T0 
        """)
    suspend fun getUsuarioListaPreciosCreacion(usuarioCode: String): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM UsuarioListaPrecios")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UsuarioListaPreciosEntity>)

    @Transaction
    suspend fun insertListaPreciosToSend(listaPreciosInfo: List<DoNuevoUsuarioItem>, userCode: String, userToCopy: String){
        val listaPreciosCopy = getListasPrecio(userToCopy)
        val listaPreciosToInsert = listaPreciosCopy.map { listaprecio->
            listaprecio.updateDataToSend(
                userCode,
                listaPreciosInfo.filter { (it.Code.toIntOrNull() ?: -1) == listaprecio.LineNum }.first()
            )
        }
        insertAll(listaPreciosToInsert)
    }

    @Query("UPDATE UsuarioListaPrecios SET AccLocked = :accLocked WHERE ListNum = :code AND Code = :userCode")
    suspend fun updateAccLocked(accLocked: String, code: String, userCode: String)

    @Update
    suspend fun update(items: List<UsuarioListaPreciosEntity>)
}