package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.UsuarioGrupoSociosEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem
import com.mobile.massiveapp.domain.model.DoUsuarioGrupoSocios
import com.mobile.massiveapp.domain.model.updateDataToSend

@Dao
interface UsuarioGrupoSociosDao:BaseDao<UsuarioGrupoSociosEntity> {

    @Query("SELECT * FROM UsuarioGrupoSocios")
    suspend fun getAll(): List<UsuarioGrupoSociosEntity>

    @Query("SELECT * FROM UsuarioGrupoSocios WHERE Code = :userCode")
    suspend fun getGrupoSocios(userCode: String): List<UsuarioGrupoSociosEntity>

    @Query("""
        SELECT 
            T0.GroupCode,
            T1.GroupName
        FROM UsuarioGrupoSocios T0
        LEFT JOIN GrupoSocio T1 ON T1.GroupCode = T0.GroupCode
        WHERE T0.Code = :usuarioCode
        """)
    suspend fun getUsuarioGrupoSocios(usuarioCode: String): List<DoUsuarioGrupoSocios>

    @Query("""
        SELECT 
            T0.GroupCode as Code,
            T0.GroupName as Name,
            CASE 
            WHEN T0.GroupCode IN (
                                SELECT 
                                    GroupCode 
                                FROM UsuarioGrupoSocios 
                                WHERE AccLocked = "N" AND Code = :usuarioCode) THEN (1)
            ELSE (0)
        END AS Checked
        FROM GrupoSocio T0
        ORDER BY GroupName
        """)
    suspend fun getUsuarioGrupoSociosCreacion(usuarioCode: String): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM UsuarioGrupoSocios")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UsuarioGrupoSociosEntity>)

    @Transaction
    suspend fun insertGrupoSociosToSend(grupoSociosInfo: List<DoNuevoUsuarioItem>, userCode: String, userToCopy:String){
        val grupoSociosCopy = getGrupoSocios(userToCopy)
        val grupoSociosToInsert = grupoSociosCopy.map { grupoSocio->
            grupoSocio.updateDataToSend(
                userCode,
                grupoSociosInfo.filter { (it.Code.toIntOrNull()?:-1) == grupoSocio.GroupCode }.first()
            )
        }
        insertAll(grupoSociosToInsert)
    }

    @Query("UPDATE UsuarioGrupoSocios SET AccLocked = :accLocked WHERE GroupCode = :code AND Code = :userCode")
    suspend fun updateAccLocked(accLocked: String, code: String, userCode: String)

    @Update
    suspend fun update(items: List<UsuarioGrupoSociosEntity>)
}