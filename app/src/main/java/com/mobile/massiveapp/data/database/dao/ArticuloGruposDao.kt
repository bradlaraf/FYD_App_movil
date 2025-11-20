package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloGruposEntity
import com.mobile.massiveapp.domain.model.DoNuevoUsuarioItem

@Dao
interface ArticuloGruposDao:BaseDao<ArticuloGruposEntity> {

    @Query("SELECT * FROM GrupoArticulo")
    suspend fun getAllArticuloGrupos(): List<ArticuloGruposEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticuloGrupos(articuloGrupos: List<ArticuloGruposEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articuloGrupos: ArticuloGruposEntity)

    @Query("""
            SELECT
                T0.ItmsGrpCod as Code,
                T0.ItmsGrpNam as Name,
                0 as Checked
            FROM GrupoArticulo T0
            ORDER BY ItmsGrpNam
            """)
    suspend fun getArticuloGruposCreacion(): List<DoNuevoUsuarioItem>

    @Query("DELETE FROM GrupoArticulo")
    suspend fun deleteAllArticuloGrupos()

    @Query("SELECT ItmsGrpNam FROM GrupoArticulo WHERE ItmsGrpCod = :itmsGrpCod")
    suspend fun getArticuloGrupoPorItmsGrpCod(itmsGrpCod: Int): String

    @Update
    suspend fun updateArticuloGrupos(articuloGrupos: List<ArticuloGruposEntity>)
}