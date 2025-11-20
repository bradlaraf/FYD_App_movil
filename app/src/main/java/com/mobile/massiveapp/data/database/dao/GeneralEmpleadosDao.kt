package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralEmpleadosEntity
import com.mobile.massiveapp.data.database.entities.GeneralVendedoresEntity


@Dao
interface GeneralEmpleadosDao:BaseDao<GeneralEmpleadosEntity> {
    @Query("""
        SELECT 
            * 
        FROM Vendedor 
        WHERE SlpCode IN (SELECT DefaultSlpCode FROM Usuario)
    """)
    suspend fun getEmpleadoDeVentas(): GeneralVendedoresEntity


    @Query("SELECT * FROM Empleado")
    suspend fun getAllGeneralEmpleados(): List<GeneralEmpleadosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGeneralEmpleados(generalEmpleados:List<GeneralEmpleadosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalEmpleados:GeneralEmpleadosEntity)

    @Query("DELETE FROM Empleado")
    suspend fun clearAllGeneralEmpleados()

    @Update
    suspend fun updateGeneralEmpleados(generalEmpleados: List<GeneralEmpleadosEntity>)
}