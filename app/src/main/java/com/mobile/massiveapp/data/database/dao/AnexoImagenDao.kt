package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.AnexoImagenEntity
import com.mobile.massiveapp.data.database.entities.ArticuloEntity
import com.mobile.massiveapp.data.database.entities.CuentasCEntity
import com.mobile.massiveapp.domain.model.DoAnexoImagen
import kotlinx.coroutines.flow.Flow

@Dao
interface AnexoImagenDao: BaseDao<AnexoImagenEntity>  {
    @Query("SELECT * FROM AnexoImagen")
    fun getAllFlow(): Flow<List<AnexoImagenEntity>>

    @Query("SELECT * FROM AnexoImagen WHERE Code = :code")
    fun getAllByCode(code: String): Flow<List<DoAnexoImagen>>

    @Query("SELECT * FROM AnexoImagen")
    suspend fun getAll(): List<AnexoImagenEntity>

    @Query("DELETE FROM AnexoImagen")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AnexoImagenEntity>)

    @Update
    suspend fun update(items: List<AnexoImagenEntity>)
}