package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloPreciosEntity
import com.mobile.massiveapp.domain.model.DoArticuloPrecios
import com.mobile.massiveapp.domain.model.DoUnidadMedidaInfo

@Dao
interface ArticuloPreciosDao: BaseDao<ArticuloPreciosEntity> {

    @Query("SELECT * FROM ArticuloPrecio")
    suspend fun getAllArticuloPrecios(): List<ArticuloPreciosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticuloPrecios(articuloPreciosEntity: List<ArticuloPreciosEntity>)

    @Query("DELETE FROM ArticuloPrecio")
    suspend fun deleteAllArticuloPrecios()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticuloPrecio(articuloPreciosEntity: ArticuloPreciosEntity)

    @Update
    suspend fun updateArticuloPrecios(articuloPreciosEntity: List<ArticuloPreciosEntity>)



        //Obtener todos los articulo precios con el nombre de lista
    @Query("""
        SELECT 
            * 
        FROM ArticuloPrecio T0 
        LEFT JOIN ListaPrecio T1 ON T0.PriceList = T1.ListNum 
        WHERE ItemCode = :itemCode
    """)
    suspend fun getAllArticuloPreciosPorItemCode(itemCode:String): List<DoArticuloPrecios>


        //Obtener un articulo precio por itemcode y priceList
    @Query("SELECT * FROM ArticuloPrecio WHERE ItemCode = :itemCode AND PriceList = :priceList")
    suspend fun getArticuloPrecioPorItemCodeYPriceList(itemCode: String, priceList: Int): ArticuloPreciosEntity

    @Query("SELECT * FROM ArticuloPrecio WHERE ItemCode = :itemCode AND PriceList = :priceList")
    suspend fun getArticuloPrecioPedido(itemCode: String, priceList: Int): ArticuloPreciosEntity


        //Obtener precio por Articulo, Lista de precio y Unidad de medida
    @Query("SELECT  " +
            "IFNULL(T0.Price / T2.AltQty, 0.0) AS 'PrecioFinal', " +
            "T3.UomEntry  " +
            "FROM ArticuloPrecio T0 " +
            "INNER JOIN Articulo T1 ON T0.ItemCode = T1.ItemCode  " +
            "INNER JOIN ArticuloGruposUMDetalle T2 ON T1.UgpEntry = T2.UgpEntry " +
            "INNER JOIN UnidadMedida T3 ON T2.UomEntry = T3.UomEntry " +
            "WHERE T0.PriceList = :listaDePrecio " +
            "AND T0.ItemCode = :itemCode " +
            "AND T3.UomCode = :unidadDeMedida")
    suspend fun getPrecioPorArticuloListaDePrecioYUnidadDeMedida(itemCode: String, listaDePrecio: Int, unidadDeMedida: String): DoUnidadMedidaInfo

}