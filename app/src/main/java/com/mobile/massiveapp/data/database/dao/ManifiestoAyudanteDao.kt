package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.CuentasCEntity
import com.mobile.massiveapp.data.database.entities.ManifiestoAyudanteEntity

@Dao
interface ManifiestoAyudanteDao:BaseDao<ManifiestoAyudanteEntity> {
    @Query("SELECT * FROM ManifiestoAyudante")
    suspend fun getAll(): List<ManifiestoAyudanteEntity>

    @Query("DELETE FROM ManifiestoAyudante")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ManifiestoAyudanteEntity>)

    @Update
    suspend fun update(items: List<ManifiestoAyudanteEntity>)
}