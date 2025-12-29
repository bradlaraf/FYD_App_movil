package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ClienteFacturaDetalleEntity
import com.mobile.massiveapp.data.database.entities.ManifiestoEntity

@Dao
interface ManifiestoDao:BaseDao<ManifiestoEntity> {
    @Query("SELECT * FROM Manifiesto")
    suspend fun getAll(): List<ManifiestoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sociedad: ManifiestoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<ManifiestoEntity>)

    @Query("DELETE FROM Manifiesto")
    suspend fun clearAll()
}