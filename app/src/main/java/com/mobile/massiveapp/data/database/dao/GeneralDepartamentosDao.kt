package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralDepartamentosEntity

@Dao
interface GeneralDepartamentosDao:BaseDao<GeneralDepartamentosEntity> {

    @Query("SELECT * FROM Departamento")
    suspend fun getAll(): List<GeneralDepartamentosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalDepartamentos: List<GeneralDepartamentosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalDepartamentos: GeneralDepartamentosEntity)

    @Query("DELETE FROM Departamento")
    suspend fun clearAll()

    @Update
    suspend fun update(generalDepartamentos: List<GeneralDepartamentosEntity>)

        //Obtener el codigo de departamento por nombre
    @Query("SELECT Code FROM Departamento WHERE Name LIKE '%'||:nombre ||'%'")
    suspend fun getDepartamentoCodePorNombre(nombre:String):String
}