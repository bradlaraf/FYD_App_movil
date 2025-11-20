package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ClienteFacturasEntity
import com.mobile.massiveapp.domain.model.DoReporteAvanceVentas
import com.mobile.massiveapp.domain.model.DoReporteAvanceVentasCabecera
import com.mobile.massiveapp.domain.model.DoReporteDetalleVentasCabecera
import com.mobile.massiveapp.domain.model.DoReporteDetalleVentas
import com.mobile.massiveapp.domain.model.DoReporteEstadoCuentaCabecera
import com.mobile.massiveapp.domain.model.DoReporteEstadoCuentaCobranzas
import com.mobile.massiveapp.domain.model.DoReporteEstadoCuentaVentas
import com.mobile.massiveapp.domain.model.DoReportePreCobranza
import com.mobile.massiveapp.domain.model.DoReporteProductosPorMarca
import com.mobile.massiveapp.domain.model.DoReporteProductosPorMarcaCabecera
import com.mobile.massiveapp.domain.model.DoReporteSaldosPorCobrar
import com.mobile.massiveapp.domain.model.DoReporteSaldosPorCobrarCabecera
import com.mobile.massiveapp.domain.model.DoReporteSaldosPorCobrarTotales
import com.mobile.massiveapp.domain.model.DoReporteVentasDiarias
import com.mobile.massiveapp.domain.model.DoReporteVentasDiariasCabecera

@Dao
interface ReportesDao {
    @Query("SELECT * FROM Factura")
    suspend fun getAll(): List<ClienteFacturasEntity>

    //VENTAS DIARIAS

        //Detalle
    @Query("""
        SELECT 
            IFNULL(T0.DocNum, '') AS 'Interno', 
            IFNULL(T0.NumAtCard, '') AS 'Sunat', 
            IFNULL(T0.DocTotal, 0.0) AS 'Importe', 
            IFNULL(T0.CardName, '') AS 'Cliente'
        FROM Factura T0 
        WHERE T0.DocDate BETWEEN :fechaInicio AND :fechaFin 
                AND SlpCode IN (SELECT Z0.DefaultSlpCode FROM Usuario Z0)
        ORDER BY T0.CardName, T0.NumAtCard DESC
    """)
    suspend fun getReporteVentasDiarias(fechaInicio: String, fechaFin: String): List<DoReporteVentasDiarias>

        //Cabecera
    @Query("""
        SELECT 
        IFNULL(SUM(T0.DocTotal), 0.0) AS 'Total'
        FROM Factura T0
        WHERE T0.DocDate BETWEEN :fechaInicio AND :fechaFin
            AND SlpCode IN (SELECT Z0.DefaultSlpCode FROM Usuario Z0)
    """)
    suspend fun getReporteVentasDiariasTotales(fechaInicio: String, fechaFin: String): DoReporteVentasDiariasCabecera

    //SALDOS POR VENDEDOR

            //Cabecera
    @Query("SELECT \n" +
            "T0.CardCode AS 'CardCode', \n" +
            "T0.CardName AS 'CardName'\n" +
            "FROM Factura T0\n" +
            "WHERE T0.DocDate ")
    suspend fun getReporteSaldosPorCobrarCabecera(): List<DoReporteSaldosPorCobrarCabecera>

            //Detalle
    @Query("""
        SELECT 
            IFNULL(T0.DocNum, '')   AS 'Clave', 
            IFNULL(T0.NumAtCard, '') AS 'Sunat', 
            IFNULL(T0.DocDate, '')  AS 'Emision', 
            IFNULL(julianday(T0.DocDueDate) - julianday(:fechaActual), 0) AS 'Dias', 
            IFNULL(T0.CardName, '') AS 'Nombre', 
            IFNULL(T0.Address, '')  AS 'Direccion', 
            IFNULL(T0.DocTotal, 0.0) AS 'Total', 
            IFNULL(T0.DocTotal - T0.PaidToDate, 0.0) AS 'Pagado', 
            IFNULL(T0.PaidToDate, 0.0) AS 'Saldo' 
        FROM Factura T0 
        WHERE T0.CardCode == :cardCode  
        AND T0.DocDate
        ORDER BY T0.CardName, T0.DocDate DESC
        """)
    suspend fun getReporteSaldosPorCobrar(cardCode: String, fechaActual: String): List<DoReporteSaldosPorCobrar>

            //Totales
    @Query("""
        SELECT 
            IFNULL(SUM(T0.DocTotal), 0.0) AS 'Total',
            IFNULL(SUM(T0.DocTotal - T0.PaidToDate), 0.0) AS 'Pagado',
            IFNULL(SUM(T0.PaidToDate), 0.0) AS 'Saldo'
        FROM Factura T0 
        WHERE T0.DocDate 
    """)
    suspend fun getReporteSaldosPorCobrarTotales(): DoReporteSaldosPorCobrarTotales


    //PRODUCTOS POR MARCA

        //Cabecera
    @Query("SELECT \n" +
            "T0.FirmCode,\n" +
            "T0.FirmName\n" +
            "FROM Fabricante T0\n" +
            "WHERE T0.FirmCode IN (SELECT X0.FirmCode FROM articulo X0 GROUP BY X0.FirmCode)")
    suspend fun getReporteProductosPorMarcaCabecera(): List<DoReporteProductosPorMarcaCabecera>

        //Detalle
    @Query("SELECT \n" +
            "T0.ItemName AS 'Producto', " +
            "T1.OnHand AS 'Stock', " +
            "(SELECT X0.Price FROM ArticuloPrecio X0 WHERE X0.PriceList = 1 AND X0.ItemCode = T0.ItemCode) AS 'PrecioCobertura', " +
            "IFNULL((SELECT X0.Price FROM ArticuloPrecio X0 WHERE X0.PriceList = 2 AND X0.ItemCode = T0.ItemCode), 0.0) AS 'PrecioMayorista' " +
            " " +
            "FROM Articulo T0 " +
            "INNER JOIN ArticuloCantidad T1 ON T1.ItemCode = T0.ItemCode " +
            "INNER JOIN ArticuloPrecio T2 ON T2.ItemCode = T0.ItemCode " +
            "WHERE T0.FirmCode = :firmCode " +
            "ORDER BY T0.ItemName")
    suspend fun getReporteProductosPorMarca(firmCode: Int): List<DoReporteProductosPorMarca>



    //AVANCE DE VENTAS

        //Cabecera
    @Query("SELECT  " +
            "T3.FirmName, " +
            "T2.FirmCode, " +
            "(SELECT SUM(Z0.Quantity) FROM FacturaDetalle Z0 WHERE Z0.DocEntry = T0.DocEntry LIMIT 1) AS 'TotalCantidad',  " +
            "SUM(T0.DocTotal) AS 'TotalImporte'  " +
            " FROM Factura T0 " +
            "INNER JOIN FacturaDetalle T1 ON T0.DocEntry = T1.DocEntry " +
            "INNER JOIN Articulo T2 ON T1.ItemCode = T2.ItemCode " +
            "INNER JOIN Fabricante T3 ON T3.FirmCode = T2.FirmCode " +
            "WHERE T0.DocDate BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY T3.FirmCode ")
    suspend fun getReporteAvancesDeVentasTitles(fechaInicio: String, fechaFin: String): List<DoReporteAvanceVentasCabecera>

        //Detalles
    @Query("SELECT " +
            "T0.ItemCode AS 'Codigo', " +
            "T1.ItemName AS 'Articulo',  " +
            "SUM(T0.Quantity) AS 'Cantidad', " +
            " SUM(T0.LineTotal) AS 'Importe' " +
            " FROM FacturaDetalle T0 " +
            "INNER JOIN Articulo T1 ON T0.ItemCode = T1.ItemCode " +
            "WHERE T1.FirmCode = :firmCode " +
            "GROUP BY T0.ItemCode")
    suspend fun getReporteAvancesDeVentasDetalle(firmCode: Int): List<DoReporteAvanceVentas>


    //ESTADO DE CUENTA

        //Cabecera
    @Query("SELECT " +
            "T0.CardCode || ' - ' || T0.CardName AS 'Cliente', " +
            "T1.ListName AS 'ListaPrecio', " +
            "T0.CreditLine AS 'LimiteCredito', " +
            "T2.PymntGroup AS 'CondicionPago' " +
            "FROM SocioNegocio T0 " +
            "INNER JOIN ListaPrecio T1 ON T0.ListNum = T1.ListNum " +
            "INNER JOIN CondicionPago T2 ON T0.GroupNum = T2.GroupNum " +
            "WHERE T0.CardCode == :cardCode")
    suspend fun getReporteEstadoDeCuentaCabecera(cardCode:String): DoReporteEstadoCuentaCabecera

        //Ventas
    @Query("SELECT " +
            "T0.DocNum AS 'Clave', " +
            "T0.NumAtCard AS 'Sunat', " +
            "T1.PymntGroup AS 'Condicion', " +
            "T2.SlpName AS 'Vendedor', " +
            "T0.TaxDate AS 'Emision', " +
            "T0.DocCur AS 'Moneda', " +
            "T0.DocTotal AS 'Total', " +
            "T0.DocTotal AS 'Saldo' " +
            "FROM Factura T0\n" +
            "INNER JOIN CondicionPago T1 ON T0.GroupNum = T1.GroupNum " +
            "INNER JOIN Vendedor T2 ON T2.SlpCode = T0.SlpCode " +
            "WHERE T0.CardCode = :cardCode")
    suspend fun getReporteEstadoDeCuentaVentas(cardCode:String): List<DoReporteEstadoCuentaVentas>


        //Cobranzas
    @Query("SELECT\n" +
            "T0.DocNum AS 'Clave',\n" +
            "T0.Series || ' - ' || T0.DocNum AS 'Sunat',\n" +
            "CASE \n" +
            "WHEN T0.TypePayment =='T' THEN 'Transferencia'\n" +
            "WHEN T0.TypePayment =='E' THEN 'Efectivo'\n" +
            "END AS 'Condicion' ,\n" +
            "T1.Name AS 'Vendedor',\n" +
            "T0.AccCreateDate AS 'Emision',\n" +
            "T0.DocDueDate AS 'FechaPago',\n" +
            "DATE(T0.DocDate) - DATE(T0.DocDueDate) AS 'NumeroDias',\n" +
            "T0.DocCurr AS 'MonedaPago',\n" +
            "CASE \n" +
            "WHEN T0.TypePayment =='T' THEN T0.TrsfrSum\n" +
            "WHEN T0.TypePayment =='E' THEN T0.CashSum\n" +
            "END AS 'Pagado'\n" +
            "FROM ClientePagos T0\n" +
            "INNER JOIN Usuario T1 ON T0.AccCreateUser == T1.Code\n" +
            "WHERE T0.CardCode == :cardCode")

    suspend fun getReporteEstadoDeCuentaCobranzas(cardCode:String): List<DoReporteEstadoCuentaCobranzas>



    //DETALLE DE VENTAS

        //Cabecera
    @Query("SELECT  " +
            "T0.DocNum AS 'Interno', " +
            "T0.NumAtCard AS 'Sunat', " +
            "T0.DocDate AS 'Fecha', " +
            "T0.CardName AS 'Cliente', " +
            "T0.DocTotal AS 'Importe', " +
            "T0.DocEntry AS 'DocEntry' " +
            "FROM Factura T0  " +
            "WHERE T0.DocDate BETWEEN :fechaInicio AND :fechaFin")
    suspend fun getReporteDetalleDeVentasCabecera(fechaInicio: String, fechaFin: String): List<DoReporteDetalleVentasCabecera>

        //Detalle
    @Query("SELECT " +
            "T0.LineNum AS 'Numero', " +
            "T0.ItemCode AS 'Codigo', " +
            "T1.ItemName AS 'Articulo', " +
            "T0.UnitMsr AS 'Unidad', " +
            "T0.Quantity AS 'Cantidad', " +
            "T2.ListName AS 'TipoPrecio', " +
            "T0.Price AS 'Precio', " +
            "T0.LineTotal AS 'Parcial' " +
            "FROM FacturaDetalle T0 " +
            "INNER JOIN Articulo T1 ON T0.ItemCode = T1.ItemCode " +
            "INNER JOIN ListaPrecio T2 ON T0.PriceList = T2.ListNum " +
            "INNER JOIN Factura T3 ON T0.DocEntry = T3.DocEntry " +
            "WHERE T3.DocDate BETWEEN :fechaInicio AND :fechaFin AND T0.DocEntry = :docEntry ")
    suspend fun getReporteDetalleDeVentas(fechaInicio: String, fechaFin: String, docEntry: String): List<DoReporteDetalleVentas>


    //PRE COBRANZA
    @Query("SELECT " +
            "T0.CardName AS 'Cliente', " +
            "T1.Name AS 'TipoPago', " +
            "T0.NumAtCard AS 'NumeroDocumento', " +
            "T0.DocTotal AS 'Importe' " +
            "FROM Factura T0 " +
            "INNER JOIN Indicador T1 ON T0.Indicator = T1.Code " +
            "WHERE DocDate BETWEEN :fechaInicio AND :fechaFin ")
    suspend fun getReportePreCobranza(fechaInicio: String, fechaFin: String): List<DoReportePreCobranza>
}
