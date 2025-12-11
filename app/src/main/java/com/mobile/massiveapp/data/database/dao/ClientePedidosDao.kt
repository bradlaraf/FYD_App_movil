package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.google.common.primitives.Ints
import com.mobile.massiveapp.data.database.entities.ClientePedidosEntity
import com.mobile.massiveapp.domain.model.DoPedidoInfoView

@Dao
interface ClientePedidosDao:BaseDao<ClientePedidosEntity> {

    @Query("""
        SELECT 
            * 
        FROM ClientePedidos 
        ORDER BY CardName
    """)
    suspend fun getAll(): List<ClientePedidosEntity>

    @Query("""
        SELECT 
            * 
        FROM ClientePedidos
        WHERE DocDate = :fechaActual
        ORDER BY CardName
    """)
    suspend fun getAll(fechaActual: String): List<ClientePedidosEntity>

    @Query("""
        -- SQLite
        -- ObtienePrecioArticulo
        -- Parámetros:
        --   :articulo     (String)
        --   :listaPrecio  (Int)
        --   :descuento    (NUMERIC)  -- no usado
        --   :usuario      (String)   -- no usado
        --   :fecha        (String, formato compatible con date())
        --   :cardCode     (String)
        --   :cantidad     (NUMERIC)
        
        SELECT
          CASE
            WHEN precioSN > 0 THEN precioSN
            WHEN precioPC > 0 THEN precioPC
            WHEN precioPP > 0 THEN precioPP
            ELSE precioLP
          END AS precioFinal
        FROM (
          -- PRECIO ESPECIAL POR SOCIO DE NEGOCIO
          SELECT
            IFNULL((
              SELECT T0.Price
              FROM PrecioEspecial T0
              WHERE T0.ListNum  = :listaPrecio
                AND T0.ItemCode = :articulo
                AND T0.CardCode = :cardCode
                AND date(:fecha) BETWEEN date(T0.ValidFrom) AND date(T0.ValidTo)
            ), 0.0) AS precioSN,
        
            -- PRECIO ESPECIAL POR PERIODO Y CANTIDAD (PRIORIZA CANTIDAD)
            IFNULL((
              SELECT T0.Price
              FROM PrecioEspecial2 T0
              INNER JOIN PrecioEspecial1 T1 ON T1.Code = T0.Code
              INNER JOIN PrecioEspecial T2  ON T2.Code = T0.Code
              WHERE date(:fecha) BETWEEN date(T2.ValidFrom) AND date(T2.ValidTo)
                AND T1.ListNum  = :listaPrecio
                AND T1.ItemCode = :articulo
                AND T1.CardCode = '*' || CAST(:listaPrecio AS TEXT)
                AND date(:fecha) BETWEEN date(T1.FromDate) AND date(T1.ToDate)
                AND T0.Amount <= :cantidad
              ORDER BY T0.Amount DESC
              LIMIT 1
            ), 0.0) AS precioPC,
        
            -- PRECIO ESPECIAL POR PERIODO (PRIORIZA PERIODO)
            IFNULL((
              SELECT T0.Price
              FROM PrecioEspecial1 T0
              INNER JOIN PrecioEspecial T1 ON T1.Code = T0.Code
              WHERE date(:fecha) BETWEEN date(T1.ValidFrom) AND date(T1.ValidTo)
                AND T0.ListNum  = :listaPrecio
                AND T0.ItemCode = :articulo
                AND T0.CardCode = '*' || CAST(:listaPrecio AS TEXT)
                AND date(:fecha) BETWEEN date(T0.FromDate) AND date(T0.ToDate)
            ), 0.0) AS precioPP,
        
            -- LISTA DE PRECIO BASE
            IFNULL((
              SELECT T0.Price
              FROM ArticuloPrecio T0
              WHERE T0.PriceList = :listaPrecio
                AND T0.ItemCode  = :articulo
            ), 0.0) AS precioLP
        ) x;
    """)
    suspend fun getPrecioArticulo(fecha: String, articulo: String, listaPrecio: String, cantidad: Int, cardCode: String):Double

    @Query("""
        SELECT 
            T1.SlpName,
            T0.AccDocEntry,
            T0.CardName,
            T0.DocCur,
            T0.DocDate,
            T0.DocDueDate,
            T0.DocDueDate,
            T0.DocTotal,
            T0.DocTotal,
            T0.VatSum,
            T0.Comments,
            T0.DocStatus
        FROM ClientePedidos T0
        LEFT JOIN Vendedor T1 ON T0.SlpCode = T1.SlpCode
        WHERE AccDocEntry = :accDocEntry
    """)
    suspend fun getPedidoInfo(accDocEntry: String): DoPedidoInfoView

    @Query("""
        SELECT
            * 
        FROM ClientePedidos 
        WHERE CANCELED = 'Y' AND DocDate = :fechaActual
    """)
    suspend fun getPedidosCancelados(fechaActual: String): List<ClientePedidosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clientePedidos: List<ClientePedidosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clientePedidos: ClientePedidosEntity)

    @Query("DELETE FROM ClientePedidos")
    suspend fun clearAll()

    @Update
    suspend fun update(clientePedidos: List<ClientePedidosEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnPedido(clientePedidos: ClientePedidosEntity)

    //Obtener un pedido por el AccDocEntry
    @Query("""
        SELECT 
            T0.AccDocEntry,
            T0.AccAction,
            T0.AccCreateDate,
            T0.AccCreateHour,
            T0.AccCreateUser,
            T0.AccError,
            T0.AccNotificado,
            T0.AccFinalized,
            T0.AccMigrated,
            T0.AccMovil,
            T0.AccUpdateDate,
            T0.AccUpdateHour,
            T0.AccUpdateUser,
            T0.Address,
            T0.Address2,
            T0.Authorized,
            T0.CANCELED,
            T0.CardCode,
            T0.AccDocEntrySN,
            T0.CardName,
            T0.CntctCode,
            T0.Comments,
            T0.DocCur,
            T0.DocDate,
            T0.DocDueDate,
            T0.DocEntry,
            T0.DocNum,
            T0.DocRate,
            T0.DocStatus,
            T0.DocTotal,
            T0.DocTotalFC,
            T0.GrosProfFC,
            T0.GrosProfit,
            T0.GroupNum,
            T0.Indicator,
            T0.LicTradNum,
            T0.NumAtCard,
            T0.PayToCode,
            T0.PriceList,
            T0.Project,
            T0.Series,
            T0.ShipToCode,
            T0.SlpCode,
            T0.TaxDate,
            T0.VatSum,
            T0.VatSumFC,
            T0.ObjType
        FROM ClientePedidos T0
        LEFT JOIN Vendedor T1 ON T0.SlpCode = T1.SlpCode 
        WHERE accDocEntry = :accDocEntry
    """)
    suspend fun getPedidoPorAccDocEntry(accDocEntry: String): ClientePedidosEntity

    //Obtener todos los pedidos NO MIGRADOS del cliente
    @Query("""
        SELECT 
        * 
        FROM ClientePedidos 
        WHERE AccMigrated = 'N' AND DocDate = :fechaActual 
        ORDER BY CardName 
    """)
    suspend fun getAllPedidosNoMigrados(fechaActual: String): List<ClientePedidosEntity>

    //Obtener todos los pedidos por CardCode
    @Query("SELECT * FROM ClientePedidos WHERE cardCode = :cardCode")
    suspend fun getAllPedidosPorCardCode(cardCode: String): List<ClientePedidosEntity>

        //Obtener todos los pedidos sin migrar
    @Query("SELECT * FROM ClientePedidos WHERE AccMigrated = 'N'")
    suspend fun getAllPedidosSinMigrar(): List<ClientePedidosEntity>


    @Query("SELECT * FROM ClientePedidos WHERE AccMigrated = 'N' AND DocDate = :fechaActual")
    suspend fun getAllPedidosNoMigradosFechaActual(fechaActual: String): List<ClientePedidosEntity>


    @Query("DELETE FROM ClientePedidos\n" +
            "WHERE DocDate NOT IN (:fechaActual)")
    suspend fun deleteAllPedidosFueraDeFechaActual(fechaActual: String)
}