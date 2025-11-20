package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralDistritosEntity

@Dao
interface GeneralDistritosDao:BaseDao<GeneralDistritosEntity> {

    @Query("SELECT * FROM Distrito T0 WHERE T0.Code LIKE :code || '%'")
    suspend fun getAllByCode(code:String): List<GeneralDistritosEntity>

    @Query("SELECT * FROM Distrito")
    suspend fun getAll(): List<GeneralDistritosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalDistritos: List<GeneralDistritosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalDistritos: GeneralDistritosEntity)

    @Query("DELETE FROM Distrito")
    suspend fun clearAll()

    @Update
    suspend fun update(generalDistritos: List<GeneralDistritosEntity>)

}