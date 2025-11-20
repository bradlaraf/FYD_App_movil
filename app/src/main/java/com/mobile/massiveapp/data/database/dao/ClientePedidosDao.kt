package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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