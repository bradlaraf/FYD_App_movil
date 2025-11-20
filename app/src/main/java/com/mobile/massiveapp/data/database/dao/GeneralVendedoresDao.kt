package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralVendedoresEntity

@Dao
interface GeneralVendedoresDao:BaseDao<GeneralVendedoresEntity> {

    @Query("SELECT * FROM Vendedor")
    suspend fun getAll(): List<GeneralVendedoresEntity>


    @Query("SELECT * FROM Vendedor WHERE SlpCode IN (SELECT DefaultSlpCode FROM Usuario)")
    suspend fun getVendedorDefault(): GeneralVendedoresEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalVendedores: List<GeneralVendedoresEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalVendedores: GeneralVendedoresEntity)

    @Query("DELETE FROM Vendedor")
    suspend fun clearAll()

    @Update
    suspend fun update(generalVendedores: List<GeneralVendedoresEntity>)
}