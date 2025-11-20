package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralDimensionesEntity

@Dao
interface GeneralDimensionesDao:BaseDao<GeneralDimensionesEntity> {

    @Query("SELECT * FROM Dimension")
    suspend fun getAll(): List<GeneralDimensionesEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalDimensiones: List<GeneralDimensionesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalDimensiones: GeneralDimensionesEntity)


    @Query("DELETE FROM Dimension")
    suspend fun clearAll()


    @Update
    suspend fun update(generalDimensiones: List<GeneralDimensionesEntity>)
}