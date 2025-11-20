package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralActividadesEEntity

@Dao
interface GeneralActividadesEDao:BaseDao<GeneralActividadesEEntity> {

    @Query("SELECT * FROM ActividadesE")
    suspend fun getAll(): List<GeneralActividadesEEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ActividadesE: List<GeneralActividadesEEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ActividadesE: GeneralActividadesEEntity)

    @Query("DELETE FROM ActividadesE")
    suspend fun clearAll()


    @Update
    suspend fun update(ActividadesE: List<GeneralActividadesEEntity>)
}