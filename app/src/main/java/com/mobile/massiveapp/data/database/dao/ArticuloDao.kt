package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ArticuloEntity
import com.mobile.massiveapp.domain.model.DoArticulo
import com.mobile.massiveapp.domain.model.DoArticuloInfo
import com.mobile.massiveapp.domain.model.DoArticuloInfoBaseView
import com.mobile.massiveapp.domain.model.DoArticuloInv
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

    @Query("""
        SELECT
            IFNULL((SELECT Z0.ItemName FROM Articulo Z0 WHERE Z0.ItemCode = T0.ItemCode), '') AS Descripcion,
            IFNULL(T0.OnHand, 0.0) AS Stock,
            IFNULL(T0.IsCommited, 0.0) AS Comprometido,
            IFNULL(T0.OnOrder, 0.0) AS Solicitado,
            IFNULL(((T0.OnHand + T0.OnOrder) - T0.IsCommited), 0.0) AS Disponible,
            IFNULL((SELECT Z0.Price FROM ArticuloPrecio Z0 WHERE Z0.ItemCode = T0.ItemCode), 0.0) AS Precio
        FROM ArticuloCantidad T0
        WHERE T0.ItemCode = :itemCode
    """)
    suspend fun getArticuloInfoBaseView(itemCode: String): DoArticuloInfoBaseView

    @Query("SELECT * FROM Articulo WHERE AccLocked ='N' ORDER BY ItemName")
    suspend fun getAllArticulos(): List<ArticuloEntity>

    @Query("""
        SELECT DISTINCT 
        T0.ItemCode,
        T0.ItemName,
        (SELECT T1.OnHand 
            FROM ArticuloCantidad T1 
            WHERE T1.ItemCode = T0.ItemCode 
              AND T1.WhsCode = 'A8000'
        ) AS 'OnHand1',
        (SELECT T1.OnHand 
            FROM ArticuloCantidad T1 
            WHERE T1.ItemCode = T0.ItemCode 
              AND T1.WhsCode = 'A0202'
        ) AS 'OnHand2',
        "" AS 'WhsName1',
        "" AS 'WhsName2',
        T2.ItmsGrpNam AS 'GrupoArticulo' 
        FROM Articulo T0
        INNER JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod
        WHERE T0.AccLocked = 'N'
        ORDER BY T0.ItemName, T0.ItemCode
        """)
    suspend fun getAllArticulosInventario(): List<DoArticuloInv>


    @Query("""
        SELECT DISTINCT
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
        SELECT DISTINCT
            T0.ItemCode,
            T0.ItemName,
            IFNULL((SELECT Z0.OnHand FROM ArticuloCantidad Z0 WHERE Z0.WhsCode = 'A8000' AND ItemCode = T0.ItemCode LIMIT 1), 0.0) AS OnHand1,
            IFNULL((SELECT Z0.OnHand FROM ArticuloCantidad Z0 WHERE Z0.WhsCode = 'A0202' AND ItemCode = T0.ItemCode LIMIT 1), 0.0) AS OnHand2,
            IFNULL((SELECT Z0.WhsName FROM Almacenes Z0 WHERE WhsCode = 'A8000' LIMIT 1), 'ALMACEN 1') AS WhsName1,
            IFNULL((SELECT Z0.WhsName FROM Almacenes Z0 WHERE WhsCode = 'A0202' LIMIT 1), 'ALMACEN 2') AS WhsName2,
            IFNULL((SELECT Z0.ItmsGrpNam FROM GrupoArticulo Z0 WHERE Z0.ItmsGrpCod = T0.ItmsGrpCod), '') AS GrupoArticulo
        FROM Articulo T0
        WHERE T0.AccLocked = 'N'
        ORDER BY T0.ItemCode, OnHand1 desc, T0.ItemName
        """)
    suspend fun getAllArticulosInvConStocks(): List<DoArticuloInv>

    @Query("""
        SELECT DISTINCT
            T0.ItemCode,
            T0.ItemName,
            IFNULL((SELECT Z0.OnHand FROM ArticuloCantidad Z0 WHERE Z0.WhsCode = 'A8000' AND ItemCode = T0.ItemCode LIMIT 1), 0.0) AS OnHand1,
            IFNULL((SELECT Z0.OnHand FROM ArticuloCantidad Z0 WHERE Z0.WhsCode = 'A0202' AND ItemCode = T0.ItemCode LIMIT 1), 0.0) AS OnHand2,
            IFNULL((SELECT Z0.WhsName FROM Almacenes Z0 WHERE WhsCode = 'A8000' LIMIT 1), 'ALMACEN 1') AS WhsName1,
            IFNULL((SELECT Z0.WhsName FROM Almacenes Z0 WHERE WhsCode = 'A0202' LIMIT 1), 'ALMACEN 2') AS WhsName2,
            IFNULL((SELECT Z0.ItmsGrpNam FROM GrupoArticulo Z0 WHERE Z0.ItmsGrpCod = T0.ItmsGrpCod), '') AS GrupoArticulo
        FROM Articulo T0
        INNER JOIN GrupoDescuentoDetalle T1 ON T1.ObjKey = T0.ItemCode
        INNER JOIN GrupoDescuento T2 ON T1.AbsEntry = T2.AbsEntry 
        WHERE T0.AccLocked = 'N'
          AND T2.ObjCode = (SELECT Z0.GroupCode FROM SocioNegocio Z0 WHERE Z0.CardCode = :cardCode LIMIT 1)
        ORDER BY T0.ItemCode, OnHand1 desc, T0.ItemName
        """)
    suspend fun getAllArticulosInvConStocks(cardCode:String): List<DoArticuloInv>

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


    @Query("""
        SELECT
            T0.ItemCode,
            T0.ItemName,
            IFNULL(T0.InvnTryUom, '') AS UnidadMedida,
            IFNULL(T1.FirmName, '') AS FirmName,
            IFNULL(T2.ItmsGrpNam, '') AS ItmsGrpNam,
            IFNULL(T3.UgpName, '') AS UgpName,
            IFNULL(T5.OnHand, 0) AS OnHand,
            IFNULL(T5.OnOrder, 0) AS OnOrder,
            IFNULL(T5.IsCommited, 0) AS IsCommited,
            IFNULL(T6.WhsName, '') AS WhsName
        FROM Articulo T0
        LEFT JOIN Fabricante T1 ON T0.FirmCode = T1.FirmCode
        LEFT JOIN GrupoArticulo T2 ON T0.ItmsGrpCod = T2.ItmsGrpCod
        LEFT JOIN GrupoUnidadMedida T3 ON T0.UgpEntry = T3.UgpEntry
        LEFT JOIN ArticuloCantidad T5 ON T0.ItemCode = T5.ItemCode
        LEFT JOIN Almacenes T6 ON T5.WhsCode = T6.WhsCode
        WHERE T0.ItemCode = :itemCode
          AND T6.WhsCode IN (
              SELECT X0.DefaultWarehouse
              FROM Usuario X0
          )
    """)
    suspend fun getArticuloInfoConUnidadDeMedida(
        itemCode: String
    ): DoArticuloInfo


    //Articulo por CardCode
    @Query("SELECT * FROM Articulo T0 WHERE T0.ItemCode = :itemCode")
    suspend fun getArticuloPorItemCode(itemCode: String): ArticuloEntity



        //Obtener el Grupo de Unidad de Medida por ItemCode
    @Query("SELECT T0.UgpCode FROM GrupoUnidadMedida T0 INNER JOIN Articulo T1 ON T0.UgpEntry = T1.UgpEntry WHERE T1.ItemCode = :itemCode")
    suspend fun getArticuloUnidadMedida(itemCode: String): String

}