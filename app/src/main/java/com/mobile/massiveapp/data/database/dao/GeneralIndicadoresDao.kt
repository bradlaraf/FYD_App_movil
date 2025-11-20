package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralIndicadoresEntity

@Dao
interface GeneralIndicadoresDao:BaseDao<GeneralIndicadoresEntity> {

    @Query("SELECT * FROM Indicador")
    suspend fun getAll(): List<GeneralIndicadoresEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalIndicadores: List<GeneralIndicadoresEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalIndicadores: GeneralIndicadoresEntity)

    @Query("DELETE FROM Indicador")
    suspend fun clearAll()

    @Update
    suspend fun update(generalIndicadores: List<GeneralIndicadoresEntity>)
}