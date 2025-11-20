package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralPaisesEntity

@Dao
interface GeneralPaisesDao:BaseDao<GeneralPaisesEntity> {
    @Query("SELECT * FROM Pais")
    suspend fun getAll(): List<GeneralPaisesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalPaises: List<GeneralPaisesEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalPaises: GeneralPaisesEntity)

    @Query("DELETE FROM Pais")
    suspend fun clearAll()


    @Update
    suspend fun update(generalPaises: List<GeneralPaisesEntity>)
}