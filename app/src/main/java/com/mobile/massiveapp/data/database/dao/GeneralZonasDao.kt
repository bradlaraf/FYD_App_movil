package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralZonasEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem

@Dao
interface GeneralZonasDao:BaseDao<GeneralZonasEntity> {

    @Query("SELECT * FROM Zona")
    suspend fun getAll(): List<GeneralZonasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<GeneralZonasEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: GeneralZonasEntity)

    @Query("DELETE FROM Zona")
    suspend fun deleteAll()

    @Update
    suspend fun update(list: List<GeneralZonasEntity>)

    @Query("""
            SELECT
                T0.Code as Code,
                T0.Name as Name,
                0 as Checked
            FROM Zona T0
            """)
    suspend fun getZonasCreacion(): List<DoNuevoUsuarioItem>

    @Query("""
            SELECT
                IFNULL(Name, '--')
            FROM Zona
            WHERE Code = :code 
            """)
    suspend fun getZona(code: String): String
}