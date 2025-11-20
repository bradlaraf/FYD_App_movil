package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.SocioNuevoAwaitEntity

@Dao
interface SocioNuevoAwaitDao {

    @Query("SELECT * FROM SocioNuevoAwait")
    suspend fun getAllSocioNuevoAwait(): List<SocioNuevoAwaitEntity>
    @Query("SELECT * FROM SocioNuevoAwait WHERE CardCode = :cardCode")
    suspend fun getInfoSocioAwait(cardCode:String): SocioNuevoAwaitEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertSocioNuevoAwait(socioNuevoAwait: SocioNuevoAwaitEntity)

    @Query("DELETE FROM SocioNuevoAwait")
    suspend fun deleteAllSocioNuevoAwait()

}