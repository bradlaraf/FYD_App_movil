package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralCentrosCEntity

@Dao
interface GeneralCentrosCDao:BaseDao<GeneralCentrosCEntity> {

    @Query("SELECT * FROM CentrosCosto")
    suspend fun getAll(): List<GeneralCentrosCEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalCentrosC: List<GeneralCentrosCEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalCentrosC: GeneralCentrosCEntity)

    @Query("DELETE FROM CentrosCosto")
    suspend fun clearAll()

    @Update
    suspend fun update(generalCentrosC: List<GeneralCentrosCEntity>)
}