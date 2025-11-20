package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralSocioGruposEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem

@Dao
interface GeneralSocioGruposDao:BaseDao<GeneralSocioGruposEntity> {
    @Query("SELECT * FROM GrupoSocio")
    suspend fun getAll(): List<GeneralSocioGruposEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalSocioGrupos: List<GeneralSocioGruposEntity>)

    @Query("""
            SELECT
                T0.GroupCode as Code,
                T0.GroupName as Name,
                0 as Checked
            FROM GrupoSocio T0
            """)
    suspend fun getSocioGruposCreacion(): List<DoNuevoUsuarioItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalSocioGrupos: GeneralSocioGruposEntity)

    @Query("DELETE FROM GrupoSocio")
    suspend fun clearAll()

    @Update
    suspend fun update(generalSocioGrupos: List<GeneralSocioGruposEntity>)
}