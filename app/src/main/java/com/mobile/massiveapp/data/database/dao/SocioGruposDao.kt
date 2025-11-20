package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.SocioGruposEntity

@Dao
interface SocioGruposDao {

    @Query("SELECT * FROM SocioGrupos")
    suspend fun getAllSocioGrupos(): List<SocioGruposEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSocioGrupos(socioGrupos:List<SocioGruposEntity>)

    @Query("DELETE FROM SocioGrupos")
    suspend fun clearAllSocioGrupos()
}