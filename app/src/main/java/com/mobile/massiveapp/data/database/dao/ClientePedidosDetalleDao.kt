package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ClientePedidosDetalleEntity
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.domain.model.DoPedidoDetalle
import com.mobile.massiveapp.domain.model.DoPedidoDetalleInfo
import com.mobile.massiveapp.domain.model.DoTotalesContenidoView
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientePedidosDetalleDao:BaseDao<ClientePedidosDetalleEntity> {

    @Query("SELECT * FROM ClientePedidosDetalle")
    suspend fun getAll(): List<ClientePedidosDetalleEntity>

    @Query("DELETE FROM ClientePedidosDetalle")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ClientePedidosDetalleEntity>)

    @Update
    suspend fun update(items: List<ClientePedidosDetalleEntity>)

    @Update
    suspend fun update(items: ClientePedidosDetalleEntity)

    @Query("""
        SELECT
            IFNULL((SELECT SUM(T0.GTotal) FROM ClientePedidosDetalle), 0.0) AS TotalAntesImpuesto,
            IFNULL((SELECT SUM(T0.LineTotal) FROM ClientePedidosDetalle), 0.0) AS TotalImpuesto,
            IFNULL((SELECT SUM(T0.LineTotal) FROM ClientePedidosDetalle), 0.0) AS TotalConImpuesto
        FROM ClientePedidosDetalle T0
        WHERE T0.AccDocEntry = :accDocEntry
         AND LineNum >= 1000 
    """)
    fun getTotalesContenidoView(accDocEntry: String): Flow<DoTotalesContenidoView>

        //Insertar un pedido detalle
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnPedidoDetalle(clientePedidoDetalle: ClientePedidosDetalleEntity)

        //Obtener el detalle del pedido por el accDocEntry
    @Query("SELECT * FROM ClientePedidosDetalle WHERE AccDocEntry LIKE :accDocEntry ORDER BY LineNum ASC")
    suspend fun getAllPedidoDetallePorAccDocEntry(accDocEntry: String):List<ClientePedidosDetalleEntity>

        //Obtener el detalle del pedido por el accDocEntry FLOW
    @Query("""
        SELECT 
            * 
        FROM ClientePedidosDetalle 
        WHERE AccDocEntry LIKE :accDocEntry 
        ORDER BY LineNum ASC
    """)
    fun getAllPedidoDetalle(accDocEntry: String):Flow<List<ClientePedidoDetalle>>

    @Query("""
        SELECT 
            * 
        FROM ClientePedidosDetalle 
        WHERE AccDocEntry LIKE :accDocEntry AND ItemCode = :itemCode 
        ORDER BY LineNum ASC
    """)
    suspend fun getAllPedidoDetalle(accDocEntry: String, itemCode: String): List<ClientePedidoDetalle>

    @Query("""
        SELECT 
            * 
        FROM ClientePedidosDetalle 
        WHERE AccDocEntry LIKE :accDocEntry AND ItemCode = :itemCode AND LineNum >= 1000 
        ORDER BY LineNum ASC
    """)
    suspend fun getAllPedidoDetalleEdicion(accDocEntry: String, itemCode: String): List<ClientePedidoDetalle>

    @Query("""
        SELECT 
            T0.ItemCode as ItemCode,
            T0.Dscription as ItemName,
            T0.LineTotal as LineTotal,
            T0.Price as Price,
            T0.UnitMsr as UnidadMedida,
            T0.Quantity as Cantidad,
            T1.WhsName as Almacen
        FROM ClientePedidosDetalle T0
        LEFT JOIN Almacenes T1 ON T0.WhsCode = T1.WhsCode 
        WHERE T0.AccDocEntry = :accDocEntry AND T0.LineNum = :lineNum
    """)
    suspend fun getPedidoDetalleInfo(accDocEntry: String, lineNum: Int):DoPedidoDetalleInfo

    @Query("""
        SELECT 
            * 
        FROM ClientePedidosDetalle 
        WHERE AccDocEntry LIKE :accDocEntry AND LineNum >= 1000 
        ORDER BY LineNum ASC
    """)
    fun getAllPedidoDetalleParaEditar(accDocEntry: String):Flow<List<ClientePedidoDetalle>>

        //Detalles de edicion  x accDocEntry
    @Query("""
            SELECT
            * 
            FROM ClientePedidosDetalle 
            WHERE AccDocEntry = :accDocEntry AND LineNum >= 1000 
            ORDER BY LineNum ASC
        """)
    suspend fun getAllDetallesEdicion(accDocEntry: String):List<ClientePedidosDetalleEntity>

        //Eliminar todos los detalles del pedido por el accDocEntry
    @Query("DELETE FROM ClientePedidosDetalle WHERE AccDocEntry = :accDocEntry")
    suspend fun deleteAllPedidoDetallePorAccDocEntry(accDocEntry: String)


        //Eliminar un pedido detalle por el AccDocEntry y LineNum
    @Query("DELETE FROM ClientePedidosDetalle WHERE AccDocEntry = :accDocEntry AND LineNum LIKE :lineNum")
    suspend fun deleteUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: Int)


        //Eliminar todos los pedido detalle sin Cabecera
    @Query("DELETE FROM ClientePedidosDetalle WHERE AccDocEntry NOT IN (SELECT AccDocEntry FROM ClientePedidos)")
    suspend fun deleteAllPedidoDetallesSinCabecera()

        //Agregar la cantidad del Pedido Detalle al IsCommited del Articulo
    @Query("""
        UPDATE ArticuloCantidad 
        SET IsCommited = IsCommited + :cantidad 
        WHERE ItemCode 
        LIKE :itemCode
    """)
    suspend fun updateIsCommitedArticulo(cantidad: Double, itemCode: String)


    @Query("""
        UPDATE ClientePedidosDetalle 
        SET LineNum = :updateLineNum
        WHERE AccDocEntry = :accDocEntry AND LineNum = :lineNum 
    """)
    suspend fun updateLineNum(updateLineNum: Int, lineNum: Int, accDocEntry: String)


    @Query("""
        SELECT 
            IFNULL(T0.ItemName, 'Nombre de Articulo') AS 'ItemName',
            IFNULL(T0.ItemCode, '--') AS 'ItemCode', 
            IFNULL(Z0.Price, 1.0) AS 'Price',
            IFNULL(Z0.LineTotal, 0.0) AS 'LineTotal',
            IFNULL(Z0.Quantity, 0.0) AS 'Quantity',
            IFNULL(T2.UgpName, '') AS 'UgpName', 
            IFNULL(T3.UomName,'') AS 'UomName', 
            IFNULL(T4.Name,'') AS 'Impuesto',
            IFNULL(T5.WhsName,'') AS 'Almacen',
            IFNULL(T6.ListName,'') AS 'ListaPrecio',
            
            IFNULL(T5.WhsCode,'') AS 'WhsCode',
            IFNULL(T6.ListNum,'') AS 'PriceList',
            IFNULL(T4.Code,'') AS 'TaxCode',
            IFNULL(T3.UomCode, '') AS 'UomCode',
            IFNULL(T3.UomEntry, -1) AS 'UomEntry'
            
        FROM ClientePedidosDetalle Z0
        INNER JOIN Articulo T0 ON T0.ItemCode = Z0.ItemCode 
        LEFT JOIN ArticuloPrecio T1 ON Z0.ItemCode = T1.ItemCode
        INNER JOIN GrupoUnidadMedida T2 ON T0.UgpEntry = T2.UgpEntry
        INNER JOIN UnidadMedida T3 ON Z0.UomEntry = T3.UomEntry
        INNER JOIN Impuesto T4 ON Z0.TaxCode = T4.Code
        INNER JOIN Almacenes T5 ON T5.WhsCode = Z0.WhsCode
        INNER JOIN ListaPrecio T6 ON Z0.PriceList = T6.ListNum
        WHERE Z0.AccDocEntry = :accDocEntry AND LineNum = :lineNum AND T1.PriceList = Z0.PriceList
    """)
    suspend fun getDetalleInfo(accDocEntry: String, lineNum: Int): DoPedidoDetalle


        //Obtener todos los AccDocEntry de los Pedidos Detalle sin cabecera

    @Query("""
            SELECT 
                T0.* 
            FROM ClientePedidosDetalle T0
            WHERE T0.AccDocEntry NOT IN (SELECT X0.AccDocEntry FROM ClientePedidos X0)""")
    suspend fun getAllAccDocEntryPedidosDetalleSinCabecera(): List<ClientePedidosDetalleEntity>

    @Query("""
            DELETE
            FROM ClientePedidosDetalle 
            WHERE LineNum >= 1000 
            AND AccDocEntry = :accDocEntry """)
    suspend fun deleteAllPedidoDetalleDuplicados(accDocEntry: String)


        //Obtener un pedido detalle por accDocEntry y LineNum
    @Query("""
            SELECT 
                * 
            FROM ClientePedidosDetalle 
            WHERE AccDocEntry = :accDocEntry AND LineNum = :lineNum
    """)
    suspend fun getUnPedidoDetallePorAccDocEntryYLineNum(accDocEntry: String, lineNum: Int): ClientePedidosDetalleEntity

}