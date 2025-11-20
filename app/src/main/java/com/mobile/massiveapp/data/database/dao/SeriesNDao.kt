package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.SeriesNEntity

@Dao
interface SeriesNDao:BaseDao<SeriesNEntity> {

    @Query("SELECT * FROM SeriesN")
    suspend fun getAll(): List<SeriesNEntity>

    @Query("DELETE FROM ConfigurarUsuarios")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SeriesNEntity>)

    @Update
    suspend fun update(items: List<SeriesNEntity>)
}