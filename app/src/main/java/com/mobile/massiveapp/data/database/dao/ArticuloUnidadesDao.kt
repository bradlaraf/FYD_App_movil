package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloUnidadesEntity
import com.mobile.massiveapp.domain.model.DoArticuloUnidades

@Dao
interface ArticuloUnidadesDao:BaseDao<ArticuloUnidadesEntity> {

    @Query("SELECT * FROM UnidadMedida")
    suspend fun getAllArticuloUnidades(): List<ArticuloUnidadesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticuloUnidades(articuloUnidadesEntity: List<ArticuloUnidadesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articuloUnidadesEntity: ArticuloUnidadesEntity)

    @Query("DELETE FROM UnidadMedida")
    suspend fun deleteAllArticuloUnidades()

    @Update
    suspend fun updateArticuloUnidades(articuloUnidadesEntity: List<ArticuloUnidadesEntity>)

    @Query("""
        SELECT
            T3.*
        FROM Articulo T0
        INNER JOIN GrupoUnidadMedida T1 ON T1.UgpEntry = T0.UgpEntry
        INNER JOIN ArticuloGruposUMDetalle T2 ON T2.UgpEntry = T1.UgpEntry
        INNER JOIN UnidadMedida T3 ON T3.UomEntry = T2.UomEntry
        WHERE T0.ItemCode = :itemCode
    """)
    suspend fun getArticuloUnidadesPorItemCode(itemCode: String): List<ArticuloUnidadesEntity>

    @Query("SELECT IFNULL(UgpName, 'MANUAL') AS 'GrupoUM' FROM GrupoUnidadMedida WHERE UgpEntry = :ugpEntry")
    suspend fun getArticuloGrupoUMPorUgpEntry(ugpEntry: String): String




        //Obtener todos los articulos por UomEntry
    @Query("SELECT * FROM UnidadMedida WHERE UomEntry = :uomEntry")
    suspend fun getAllArticuloUnidadesPorUomEntry(uomEntry: Int): List<ArticuloUnidadesEntity>

        //Obtener la Unidad de Medida por UomCode
    @Query("SELECT * FROM UnidadMedida WHERE UomCode = :uomCode")
    suspend fun getArticuloUnidadMedidaPorUomCode(uomCode: String): DoArticuloUnidades

    @Query("SELECT T0.* FROM UnidadMedida T0 WHERE T0.UomEntry IN (SELECT X0.LineNum FROM ArticuloGruposUMDetalle X0 WHERE UgpEntry = :ugpEntry)")
    suspend fun getArticuloUnidadMedidaPorItemCode(ugpEntry: String): List<ArticuloUnidadesEntity>


        //Obtener Unidades de Medida por Grupo UM
    @Query("SELECT T0.* FROM UnidadMedida T0 WHERE T0.UomEntry IN (SELECT X0.LineNum FROM ArticuloGruposUMDetalle X0 WHERE UgpEntry = :grupoUnidadMedida)")
    suspend fun getArticuloUnidadMedidaPorUgpEntry(grupoUnidadMedida: String): List<ArticuloUnidadesEntity>


}