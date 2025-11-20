package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.SociedadEntity

@Dao
interface SociedadDao:BaseDao<SociedadEntity> {

    @Query("SELECT * FROM Sociedad")
    suspend fun getAll(): List<SociedadEntity>

    @Query("""
        SELECT 
            * 
        FROM Sociedad
        LIMIT 1
    """)
    suspend fun getSociedadDefault(): SociedadEntity

    @Query("SELECT * FROM Sociedad WHERE TaxIdNum = :taxIdNum")
    suspend fun getSociedadPorTaxIdNum(taxIdNum: String): SociedadEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<SociedadEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sociedad: SociedadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(sociedad: SociedadEntity)

    @Query("DELETE FROM Sociedad")
    suspend fun clearAll()


    @Update
    suspend fun update(sociedad: List<SociedadEntity>)

}