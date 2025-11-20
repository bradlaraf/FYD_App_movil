package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloEntity
import com.mobile.massiveapp.domain.model.DoArticulo
import com.mobile.massiveapp.domain.model.DoArticuloInfo
import com.mobile.massiveapp.domain.model.DoArticuloInventario
import com.mobile.massiveapp.domain.model.DoArticuloPedidoInfo
import com.mobile.massiveapp.domain.model.DoUnidadMedidaInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticuloDao: BaseDao<ArticuloEntity> {

    @Query("SELECT COUNT(ItemCode) FROM Articulo")
    fun getAllCountArticulos(): Flow<Int>

    @Query("""
        SELECT 
            *   
        FROM Articulo T0
        ORDER BY ItemName, ItemCode
        
    """)
    suspend fun getAllArticulosWithLimit(): List<ArticuloEntity>

    @Query("""
        SELECT 
        T0.ItemCode,
        T0.ItemName AS 'ItemName',
        T1.OnHand AS 'OnHand',
        T2.ItmsGrpNam AS 'GrupoArticulo' 
        FROM Articulo T0
        INNER JOIN ArticuloCantidad T1 ON T0.ItemCode = T1.ItemCode
        INNER JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod
        WHERE T0.AccLocked = 'N' AND T0.ItemName LIKE '%' || :text || '%' OR T0.ItemCode LIKE '%' || :text || '%'
        ORDER BY ItemName
    """)
    suspend fun searchArticulos(text: String): List<DoArticuloInventario>



    @Query("SELECT * FROM Articulo WHERE AccLocked ='N' ORDER BY ItemName")
    suspend fun getAllArticulos(): List<ArticuloEntity>

    @Query("""
        SELECT 
        T0.ItemCode,
        T0.ItemName,
        T1.OnHand AS 'OnHand',
        T2.ItmsGrpNam AS 'GrupoArticulo' 
        FROM Articulo T0
        INNER JOIN ArticuloCantidad T1 ON T0.ItemCode = T1.ItemCode
        INNER JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod
        WHERE T0.AccLocked = 'N'
        ORDER BY T0.ItemName, T1.OnHand desc, T0.ItemCode
        """)
    suspend fun getAllArticulosInventario(): List<DoArticuloInventario>


    @Query("""
        SELECT 
        T0.ItemCode,
        T0.ItemName,
        T1.OnHand AS 'OnHand',
        T2.ItmsGrpNam AS 'GrupoArticulo' 
        FROM Articulo T0
        INNER JOIN ArticuloCantidad T1 ON T0.ItemCode = T1.ItemCode
        INNER JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod
        WHERE T0.AccLocked = 'N' AND T1.OnHand > 0.0
        ORDER BY T0.ItemName, T1.OnHand desc, T0.ItemCode
        """)
    suspend fun getAllArticulosInvConStock(): List<DoArticuloInventario>

    @Query("""
        SELECT 
        T0.ItemCode,
        T0.ItemName,
        T1.OnHand  AS 'OnHand',
        T2.ItmsGrpNam AS 'GrupoArticulo' 
        FROM Articulo T0
        INNER JOIN ArticuloCantidad T1 ON T0.ItemCode = T1.ItemCode
        INNER JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod
        WHERE T0.AccLocked = 'N' AND T1.OnHand <= 0.0 
        ORDER BY T0.ItemName, T1.OnHand desc, T0.ItemCode
        """)
    suspend fun getAllArticulosInvSinStock(): List<DoArticuloInventario>

    @Query("SELECT " +
            "T0.FirmCode, " +
            "T0.InvntItem, " +
            "T0.InvntryUom, " +
            "T0.ItemCode, " +
            "T0.ItemName, " +
            "T0.ItmsGrpCod, " +
            "T0.IuoMEntry, " +
            "T0.SalUnitMsr, " +
            "T0.SuoMEntry, " +
            "T0.UgpEntry, " +
            "T1.OnHand AS 'OnHand', " +
            "T0.AccLocked, " +
            "T2.ItmsGrpNam AS 'GrupoArticulo' " +
            "FROM Articulo T0 " +
            "INNER JOIN ArticuloCantidad T1 ON T0.ItemCode = T1.ItemCode " +
            "INNER JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod " +
            "WHERE T0.AccLocked ='N' AND T1.WhsCode IN (SELECT DefaultWarehouse FROM Usuario) " +
            "ORDER BY T0.ItemName")
    suspend fun getAllArticulosPedidos(): List<DoArticulo>



    @Query("SELECT \n" +
            "T0.OnHand \n" +
            "FROM ArticuloCantidad T0\n" +
            "INNER JOIN Articulo T1 ON T0.ItemCode = T1.ItemCode\n" +
            "INNER JOIN ArticuloPrecio T2 ON T2.ItemCode = T1.ItemCode\n" +
            "WHERE T2.PriceList IN (SELECT X0.DefaultPriceList FROM Usuario X0) AND T1.ItemCode = :itemCode")
    suspend fun getOnHandPorCardCode(itemCode: String): Double

/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articulos:List<ArticuloEntity>)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articulos: ArticuloEntity)

    @Query("DELETE FROM Articulo")
    suspend fun clearAll()

    @Update
    suspend fun update(articulos: List<ArticuloEntity>)



    //Articulo info para creacion de pedido
    @Query("""
        SELECT 
            IFNULL(T0.ItemName, 'Nombre de Articulo') AS 'ItemName', 
            IFNULL(T0.SalUnitMsr, '') AS 'SalUnitMsr',
            T0.ItemCode, 
            IFNULL(T1.Price, 1.0) AS 'price', 
            IFNULL(T2.UgpName, '') AS 'UgpName', 
            IFNULL(T3.UomName,'') AS 'UomName', 
            IFNULL(T3.UomCode, '') AS 'UomCode',
            IFNULL(T3.UomEntry, -1) AS 'UomEntry'
        FROM Articulo T0
        LEFT JOIN ArticuloPrecio T1 ON T0.ItemCode = T1.ItemCode
        INNER JOIN GrupoUnidadMedida T2 ON T0.UgpEntry = T2.UgpEntry
        INNER JOIN UnidadMedida T3 ON T0.IuoMEntry = T3.UomEntry
        WHERE T0.ItemCode = :itemCode AND T1.PriceList IN (SELECT DefaultPriceList FROM Usuario)
    """)
    suspend fun getArticuloInfoPedidoConUnidadMedida(itemCode: String): DoArticuloPedidoInfo



    //Articulo info para creacion de pedido

    @Query("""
        SELECT 
            IFNULL(T0.ItemName, 'Nombre de Articulo') AS 'ItemName', 
            IFNULL(T0.SalUnitMsr, '') AS 'SalUnitMsr', 
            T0.ItemCode, 
            IFNULL(T1.Price, 1.0) AS 'price', 
            'MANUAL' AS 'UgpName', 
            '' AS 'UomCode',
            T0.SalUnitMsr AS 'UomName', 
            -1 AS 'UomEntry'
        FROM Articulo T0 
        LEFT JOIN ArticuloPrecio T1 ON T0.ItemCode = T1.ItemCode 
        WHERE T0.ItemCode = :itemCode AND T1.PriceList IN (SELECT X0.DefaultPriceList FROM Usuario X0)
    """)
    suspend fun getArticuloInfoPedidoSinUnidadMedida(itemCode: String): DoArticuloPedidoInfo

    @Query("""
        SELECT 
        CASE 
            WHEN T0.UgpEntry = -1 THEN (
                  SELECT
                  X0.Price * X1.AltQty AS 'Precio FInal'
                  FROM ArticuloPrecio X0
                  INNER JOIN ArticuloGruposUMDetalle X1 ON X1.UgpEntry = T0.UgpEntry
                  WHERE X0.PriceList = :listaPrecio
                  AND X0.ItemCode = T0.ItemCode
            )
            ELSE (SELECT
                  X0.Price / X1.AltQty AS 'Precio FInal'
                  FROM ArticuloPrecio X0
                  INNER JOIN ArticuloGruposUMDetalle X1 ON X1.UgpEntry = T0.UgpEntry
                  INNER JOIN GrupoUnidadMedida X2 ON X2.UgpEntry = X1.UgpEntry
                  INNER JOIN UnidadMedida X3 ON X3.UomEntry = X1.UomEntry
                  WHERE X0.PriceList = :listaPrecio
                  AND X0.ItemCode = T0.ItemCode
                  AND X3.UomName = :unidadMedida)
        END AS PrecioFinal,
        (SELECT X0.UomEntry FROM UnidadMedida X0 WHERE X0.UomName = :unidadMedida) AS 'UomEntry'
        FROM Articulo T0
        WHERE T0.ItemCode = :itemCode
    """)
    suspend fun getArticuloPrecioPedido(itemCode: String, unidadMedida: String, listaPrecio: Int):DoUnidadMedidaInfo


    @Query("SELECT " +
            "T0.ItemCode, " +
            "T0.ItemName, " +
            "T0.InvnTryUom AS 'UnidadMedida', " +
            "T1.FirmName, " +
            "T2.ItmsGrpNam, " +
            "T3.UgpName, " +
            "T5.OnHand, " +
            "T5.OnOrder, " +
            "T5.IsCommited, " +
            "T6.WhsName " +
            "FROM Articulo T0 " +
            "INNER JOIN Fabricante T1 ON T0.FirmCode = T1.FirmCode " +
            "INNER JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod " +
            "INNER JOIN GrupoUnidadMedida T3 ON T0.UgpEntry = T3.UgpEntry " +
            "INNER JOIN ArticuloCantidad T5 ON T0.ItemCode = T5.ItemCode " +
            "INNER JOIN Almacenes T6 ON T5.WhsCode = T6.WhsCode " +
            "WHERE T0.ItemCode = :itemCode AND T6.WhsCode IN (SELECT X0.DefaultWarehouse FROM Usuario X0) ")
    suspend fun getArticuloInfoConUnidadDeMedida(itemCode: String): DoArticuloInfo

        //Articulo info











    //Articulo por CardCode
    @Query("SELECT * FROM Articulo T0 WHERE T0.ItemCode = :itemCode")
    suspend fun getArticuloPorItemCode(itemCode: String): ArticuloEntity



        //Obtener el Grupo de Unidad de Medida por ItemCode
    @Query("SELECT T0.UgpCode FROM GrupoUnidadMedida T0 INNER JOIN Articulo T1 ON T0.UgpEntry = T1.UgpEntry WHERE T1.ItemCode = :itemCode")
    suspend fun getArticuloUnidadMedida(itemCode: String): String

}