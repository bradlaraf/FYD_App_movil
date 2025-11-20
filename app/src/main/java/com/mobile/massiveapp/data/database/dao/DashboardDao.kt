package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardDao {
        //INFO DE LAS TABLAS


        //PEDIDOS
    @Query("""
        SELECT
            IFNULL(SUM(DocTotal) , 0.0) AS 'Total' 
        FROM ClientePedidos
        WHERE DocDate = :fechaActual AND CANCELED = 'N'
        """)
    fun getTotalAcumuladoPedidos(fechaActual: String): Flow<Double>

    @Query("""
        SELECT 
            IFNULL(COUNT(DocTotal) , 0) AS 'Total' 
        FROM ClientePedidos 
        WHERE DocDate = :fechaActual AND CANCELED = 'N'
    """)
    fun getTotalPedidos(fechaActual: String): Flow<Int>



        //PAGOS
    @Query("""
        SELECT
            IFNULL(SUM(CashSum) , 0) + IFNULL(SUM(TrsfrSum) , 0) AS 'Total' 
        FROM ClientePagos 
        WHERE DocDate = :fechaActual AND Canceled = 'N'
        """)
    fun getTotalAcumuladoPagos(fechaActual: String): Flow<Double>

    @Query("""
        SELECT 
            IFNULL(COUNT(AccDocEntry) , 0.0) AS 'Total' 
        FROM ClientePagos 
        WHERE DocDate = :fechaActual AND Canceled = 'N'
    """)
    fun getTotalPagos(fechaActual: String): Flow<Int>



        //FACTURAS
    @Query("""
            SELECT 
            IFNULL(COUNT(T0.DocEntry) , 0) AS 'Total' 
            FROM Factura T0
        """)
    fun getTotalFacturas(): Flow<Int>

    @Query("""
            SELECT 
            IFNULL(SUM(T0.PaidToDate) , 0.0) AS 'Total' 
            FROM Factura T0
        """)
    fun getTotalAcumuladoFacturas(): Flow<Double>

}