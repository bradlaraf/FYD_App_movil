package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloCantidadesEntity

@Dao
interface ArticuloCantidadesDao:BaseDao<ArticuloCantidadesEntity> {

    @Query("SELECT * FROM ArticuloCantidad")
    suspend fun getAllArticuloCantidades(): List<ArticuloCantidadesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticuloCantidades(articuloCantidades: List<ArticuloCantidadesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticuloCantidad(articuloCantidades: ArticuloCantidadesEntity)

    @Query("DELETE FROM ArticuloCantidad")
    suspend fun deleteAllArticuloCantidades()


    @Update
    suspend fun updateArticuloCantidades(articuloCantidades: List<ArticuloCantidadesEntity>)


        //Obtener un articulo cantidad por itemcode y WhsCode
    @Query("SELECT * FROM ArticuloCantidad WHERE ItemCode = :itemCode AND WhsCode = :whsCode")
    suspend fun getArticuloCantidadPorItemCodeYWhsCode(itemCode: String, whsCode: String): ArticuloCantidadesEntity

    @Query("""
        SELECT
            T3.AltQty * ((T0.OnHand + T0.OnOrder) - T0.IsCommited)
        FROM ArticuloCantidad T0
        INNER JOIN Articulo T1 ON T1.ItemCode = T0.ItemCode
        INNER JOIN GrupoUnidadMedida T2 ON T2.UgpEntry = T1.UgpEntry
        INNER JOIN ArticuloGruposUMDetalle T3 ON T2.UgpEntry = T3.UgpEntry
        INNER JOIN UnidadMedida T4 ON T4.UomEntry = T3.UomEntry
        WHERE T0.ItemCode = :itemCode
            AND T4.UomName = :unidadMedida
            AND T0.WhsCode = :whsCode
    """)
    suspend fun getArticuloCantidadPedido(itemCode: String, unidadMedida: String, whsCode: String):Double

}