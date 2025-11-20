package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.UsuarioGrupoArticulosEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.domain.model.DoUsuarioGrupoArticulos
import com.mobile.massiveapp.domain.model.updateDataToSend

@Dao
interface UsuarioGrupoArticuloDao:BaseDao<UsuarioGrupoArticulosEntity> {

    @Query("SELECT * FROM UsuarioGrupoArticulos")
    suspend fun getAll(): List<UsuarioGrupoArticulosEntity>

    @Query("SELECT * FROM UsuarioGrupoArticulos WHERE Code = :userCode")
    suspend fun getGrupoArticulos(userCode: String): List<UsuarioGrupoArticulosEntity>

    @Query("""
        SELECT 
            T0.ItmsGrpCod,
            T1.ItmsGrpNam
        FROM UsuarioGrupoArticulos T0
        LEFT JOIN GrupoArticulo T1 ON T1.ItmsGrpCod = T0.ItmsGrpCod
        WHERE T0.Code = :usuarioCode
        """)
    suspend fun getUsuarioGrupoArticulos(usuarioCode: String): List<DoUsuarioGrupoArticulos>

    @Query("""
        SELECT 
            T0.ItmsGrpCod as Code,
            T0.ItmsGrpNam as Name,
            CASE 
            WHEN T0.ItmsGrpCod IN (
                                SELECT 
                                    ItmsGrpCod 
                                FROM UsuarioGrupoArticulos 
                                WHERE AccLocked = "N" AND Code = :usuarioCode) THEN (1)
            ELSE (0)
        END AS Checked
        FROM GrupoArticulo T0
        ORDER BY ItmsGrpNam
        """)
    suspend fun getUsuarioGrupoArticulosCreacion(usuarioCode: String): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM UsuarioGrupoArticulos")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UsuarioGrupoArticulosEntity>)

    @Transaction
    suspend fun insertGrupoArticulosToSend(grupoArticuloInfo: List<DoNuevoUsuarioItem>, userCode: String, userToCopy: String){
        val grupoArticuloCopy = getGrupoArticulos(userCode)
        val grupoArticuloToInsert = grupoArticuloCopy.map { grupoArticulo->
            grupoArticulo.updateDataToSend(
                userCode,
                grupoArticuloInfo.filter { (it.Code.toIntOrNull()?:-1) == grupoArticulo.ItmsGrpCod }.first()
            )
        }
        insertAll(grupoArticuloToInsert)
    }

    @Update
    suspend fun update(items: List<UsuarioGrupoArticulosEntity>)

    @Query("UPDATE UsuarioGrupoArticulos SET AccLocked = :accLocked WHERE ItmsGrpCod = :code and Code = :userCode")
    suspend fun updateAccLocked(accLocked: String, code: String, userCode: String)

}