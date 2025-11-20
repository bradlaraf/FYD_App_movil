package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralProvinciasEntity


@Dao
interface GeneralProvinciasDao:BaseDao<GeneralProvinciasEntity> {

    @Query("SELECT * FROM Provincia T0 WHERE T0.Code LIKE :code || '%'")
    suspend fun getAllByCode(code:String): List<GeneralProvinciasEntity>

    @Query("SELECT * FROM Provincia")
    suspend fun getAll(): List<GeneralProvinciasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalProvincias: List<GeneralProvinciasEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalProvincias: GeneralProvinciasEntity)


    @Update
    suspend fun update(generalProvincias: List<GeneralProvinciasEntity>)

    @Query("DELETE FROM Provincia")
    suspend fun clearAll()
}