package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralImpuestosEntity

@Dao
interface GeneralImpuestosDao:BaseDao<GeneralImpuestosEntity> {

    @Query("""
        SELECT 
            *
        FROM Impuesto T0
        WHERE T0.Code <> 'IGV'
    """)
    suspend fun getAll(): List<GeneralImpuestosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalImpuestos: List<GeneralImpuestosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalImpuestos: GeneralImpuestosEntity)

    @Query("DELETE FROM Impuesto")
    suspend fun clearAll()

    @Update
    suspend fun update(generalImpuestos: List<GeneralImpuestosEntity>)

        //Obtener el impuesto por default
    @Query("""
        SELECT 
            * 
        FROM Impuesto 
        WHERE Code = 'IGV_18'
        """)
    suspend fun getImpuestoDefault(): GeneralImpuestosEntity
}