package com.mobile.massiveapp.data.repositories

import com.mobile.massiveapp.data.database.dao.ReportesDao
import com.mobile.massiveapp.data.network.ReportesService
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoReporteAvanceVentas
import com.mobile.massiveapp.domain.model.DoReporteAvanceVentasCabecera
import com.mobile.massiveapp.domain.model.DoReporteDetalleVentasCabecera
import com.mobile.massiveapp.domain.model.DoReporteDetalleVentas
import com.mobile.massiveapp.domain.model.DoReporteEstadoCuentaVentas
import com.mobile.massiveapp.domain.model.DoReporteEstadoCuentaCabecera
import com.mobile.massiveapp.domain.model.DoReporteEstadoCuentaCobranzas
import com.mobile.massiveapp.domain.model.DoReportePreCobranza
import com.mobile.massiveapp.domain.model.DoReporteProductosPorMarca
import com.mobile.massiveapp.domain.model.DoReporteProductosPorMarcaCabecera
import com.mobile.massiveapp.domain.model.DoReporteSaldosPorCobrar
import com.mobile.massiveapp.domain.model.DoReporteSaldosPorCobrarCabecera
import com.mobile.massiveapp.domain.model.DoReporteSaldosPorCobrarTotales
import com.mobile.massiveapp.domain.model.DoReporteVentasDiarias
import com.mobile.massiveapp.domain.model.DoReporteVentasDiariasCabecera
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.domain.model.ReporteAvanceVentas
import com.mobile.massiveapp.domain.model.ReporteDetalleVenta
import com.mobile.massiveapp.domain.model.ReporteEstadoCuenta
import com.mobile.massiveapp.domain.model.ReportePreCobranza
import com.mobile.massiveapp.domain.model.ReporteSaldosPorCobrar
import com.mobile.massiveapp.domain.model.ReporteVentasDiarias
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import javax.inject.Inject
class ReportesRepository @Inject constructor(
    private val reportesDao: ReportesDao,
    private val reportesService: ReportesService
) {

        //ESTADO DE CUENTA

    suspend fun getReporteEstadoCuentaFromWS(
        codigoCliente:String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ): List<ReporteEstadoCuenta> =
        try {
            reportesService.getReporteEstadoCuenta(
                codigoCliente,
                configuracion =configuracion,
                usuario = usuario,
                url =  url,
                timeout =  timeout
            )
        }catch (e:Exception){
            emptyList()
        }


    suspend fun getReporteEstadoCuentaVentas(cardCode: String): List<DoReporteEstadoCuentaVentas> =
        try {
            val listaReporte = reportesDao.getReporteEstadoDeCuentaVentas(cardCode)
            listaReporte.forEach {
                it.Total.format(2)
                it.Saldo.format(2)
            }
            listaReporte
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    suspend fun getReporteEstadoCuentaCobranzas(cardCode: String): List<DoReporteEstadoCuentaCobranzas> =
        try {
            val listaReporte = reportesDao.getReporteEstadoDeCuentaCobranzas(cardCode)
            listaReporte.forEach {
                it.Pagado.format(2)
            }
            listaReporte
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    suspend fun getReporteEstadoCuentaCabecera(cardCode: String): DoReporteEstadoCuentaCabecera =
        try {
            val reporte = reportesDao.getReporteEstadoDeCuentaCabecera(cardCode)
            reporte.LimiteCredito.format(2)
            reporte
        } catch (e: Exception){
            e.printStackTrace()
            DoReporteEstadoCuentaCabecera()
        }

        //SALDOS POR COBRAR

    suspend fun getReporteSaldosPorCobrarFromWS(
        fechaInicio: String,
        fechaFin: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L
    ): List<ReporteSaldosPorCobrar> =
        try {
            reportesService.getReporteSaldosPorCobrar(
                fechaInicial = fechaInicio,
                fechaFinal = fechaFin,
                configuracion =configuracion,
                usuario = usuario,
                url =  url,
                timeout =  timeout
            )
        } catch (e:Exception){
            emptyList()
        }
    suspend fun getReporteSaldosPorCobrar(fechaInicio: String, fechaFin: String, cardCode: String): List<DoReporteSaldosPorCobrar> =
        try {
            val fechaActual = getFechaActual()
            val listaReporte = reportesDao.getReporteSaldosPorCobrar(cardCode, fechaActual)
            /*listaReporte.forEach {
                it.Total.format(2)
                it.Saldo.format(2)
                it.Pagado.format(2)
            }*/
            listaReporte

        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    suspend fun getReporteSaldosPorCobrarCabecera(fechaInicio: String, fechaFin: String): List<DoReporteSaldosPorCobrarCabecera> =
        try {
            reportesDao.getReporteSaldosPorCobrarCabecera()
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    suspend fun getReporteSaldosPorCobrarTotales(fechaInicio: String, fechaFin: String): DoReporteSaldosPorCobrarTotales =
        try {
            val reporte = reportesDao.getReporteSaldosPorCobrarTotales()
            reporte.Saldo.format(2)
            reporte.Pagado.format(2)
            reporte.Pagado.format(2)
            reporte
        } catch (e: Exception){
            e.printStackTrace()
            DoReporteSaldosPorCobrarTotales(0.0, 0.0, 0.0)
        }

        //PRODUCTOS POR MARCA
    suspend fun getReporteProductosPorMarca(firmCode: Int): List<DoReporteProductosPorMarca> =
        try {
            val listaReporte = reportesDao.getReporteProductosPorMarca(firmCode)
            listaReporte.forEach {
                it.PrecioCobertura.format(2)
                it.Stock.format(2)
                it.PrecioMayorista.format(2)
            }
            listaReporte
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    suspend fun getReporteProductosPorMarcaCabecera(): List<DoReporteProductosPorMarcaCabecera> =
        try {
            reportesDao.getReporteProductosPorMarcaCabecera()
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

        //PRE COBRANZA

    suspend fun getReportePreCobranzaFromWS(
        fechaInicio: String,
        fechaFin: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReportePreCobranza> =
        try {
            reportesService.getReportePreCobranza(
                fechaInicial = fechaInicio,
                fechaFinal = fechaFin,
                configuracion =configuracion,
                usuario = usuario,
                url =  url,
                timeout =  timeout
            )
        } catch (e:Exception){
            emptyList()
        }

    suspend fun getReportePreCobranza(fechaInicio: String, fechaFin: String): List<DoReportePreCobranza> =
        try {
            reportesDao.getReportePreCobranza(fechaInicio, fechaFin)
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }


        //AVANCES DE VENTAS
    suspend fun getReporteAvanceVentasFromWS(
        fechaInicio: String,
        fechaFin: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReporteAvanceVentas> =
        try {
            reportesService.getReporteAvanceVentas(
                fechaInicial = fechaInicio,
                fechaFinal = fechaFin,
                configuracion =configuracion,
                usuario = usuario,
                url =  url,
                timeout =  timeout
            )
        }catch (e:Exception){
            emptyList()
        }
    suspend fun getReporteAvancesDeVentas(firmCode: Int): List<DoReporteAvanceVentas> =
        try {
            val listaReporte = reportesDao.getReporteAvancesDeVentasDetalle(firmCode)
            listaReporte.forEach {
                it.Cantidad.format(2)
                it.Importe.format(2)
            }
            listaReporte
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    suspend fun getReporteAvancesDeVentasCabecera(fechaInicio: String, fechaFin: String): List<DoReporteAvanceVentasCabecera> =
        try {
            val listaReporte = reportesDao.getReporteAvancesDeVentasTitles(fechaInicio, fechaFin)
            listaReporte.forEach {
                it.TotalCantidad.format(2)
                it.TotalImporte.format(2)
            }
            listaReporte
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }


        //VENTAS DIARIAS
    suspend fun getReporteVentasDiariasFromWS(
        fechaInicio: String,
        fechaFin: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ): List<ReporteVentasDiarias> =
        try {
            reportesService.getReporteVentaDiaria(
                fechaInicial = fechaInicio,
                fechaFinal = fechaFin,
                configuracion =configuracion,
                usuario = usuario,
                url =  url,
                timeout =  timeout
            )
        } catch (e:Exception){
            e.printStackTrace()
            emptyList()
        }
    suspend fun getReporteVentasDiarias(fechaInicio: String, fechaFin: String): List<DoReporteVentasDiarias> =
        try {
            val listaReporte = reportesDao.getReporteVentasDiarias(fechaInicio, fechaFin)
            listaReporte.forEach {
                it.Importe.format(2)
            }
            listaReporte
        }catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }

    suspend fun getReporteVentasDiariasCabecera(fechaInicio: String, fechaFin: String): DoReporteVentasDiariasCabecera =
        try {
            val reporteCabecera = reportesDao.getReporteVentasDiariasTotales(fechaInicio, fechaFin)
            reporteCabecera.Total.format(2)
            reporteCabecera
        } catch (e: Exception){
            e.printStackTrace()
            DoReporteVentasDiariasCabecera( 0.0)
        }


        //DETALLE DE VENTAS
    suspend fun getReporteDetalleVentasFromWS(
        fechaInicio: String,
        fechaFin: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReporteDetalleVenta> =
        try {
            reportesService.getReporteDetalleVenta(
                fechaInicial = fechaInicio,
                fechaFinal = fechaFin,
                configuracion =configuracion,
                usuario = usuario,
                url =  url,
                timeout =  timeout
            )
        } catch (e:Exception){
            emptyList()
        }

    suspend fun getReporteDetalleDeVentas(fechaInicio: String, fechaFin: String, docEntry: String): List<DoReporteDetalleVentas> =
        try {
            val listaReporte = reportesDao.getReporteDetalleDeVentas(fechaInicio, fechaFin, docEntry)
            listaReporte.forEach {
                it.Cantidad.format(2)
                it.Precio.format(2)
                it.Parcial.format(2)
            }
            listaReporte
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }



    suspend fun getReporteDetalleDeVentasCabecera(fechaInicio: String, fechaFin: String): List<DoReporteDetalleVentasCabecera> =
        try {
            val listaReporteCabecera = reportesDao.getReporteDetalleDeVentasCabecera(fechaInicio, fechaFin)
            listaReporteCabecera.forEach {
                it.Importe.format(2)
            }
            listaReporteCabecera
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
}

