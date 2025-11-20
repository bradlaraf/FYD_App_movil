package com.mobile.massiveapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.massiveapp.domain.reportes.GenerateReporteAvancesVentasUseCase
import com.mobile.massiveapp.domain.reportes.GenerateReporteDetalleVentasUseCase
import com.mobile.massiveapp.domain.reportes.GenerateReporteEstadoCuentaUseCase
import com.mobile.massiveapp.domain.reportes.GenerateReportePedidosUseCase
import com.mobile.massiveapp.domain.reportes.GenerateReportePreCobranzaUseCase
import com.mobile.massiveapp.domain.reportes.GenerateReporteProductosPorMarcaUseCase
import com.mobile.massiveapp.domain.reportes.GenerateReporteSaldosPorCobrarUseCase
import com.mobile.massiveapp.domain.reportes.GenerateReporteVentasDiariasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportesViewModel @Inject constructor(
    private val generateReportePedidosUseCase: GenerateReportePedidosUseCase,
    private val generateReporteSaldosPorCobrarUseCase: GenerateReporteSaldosPorCobrarUseCase,
    private val generateReporteProductosPorMarcaUseCase: GenerateReporteProductosPorMarcaUseCase,
    private val generateReporteEstadoCuentaUseCase: GenerateReporteEstadoCuentaUseCase,
    private val generateReportePreCobranzaUseCase: GenerateReportePreCobranzaUseCase,
    private val generateReporteAvancesVentasUseCase: GenerateReporteAvancesVentasUseCase,
    private val generateReporteVentasDiariasUseCase: GenerateReporteVentasDiariasUseCase,
    private val generateReporteDetalleVentasUseCase: GenerateReporteDetalleVentasUseCase
):ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

        //Genera el REPORTE DE PEDIDOS - Prueba
    val dataGenerateReportePedidos = MutableLiveData<Boolean>()

    fun generateReportePedidos(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReportePedidosUseCase()

            result.let {
                dataGenerateReportePedidos.postValue(result)
                isLoading.postValue(false)
            }
        }
    }
        //Genera el REPORTE DE SALDOS POR COBRAR
    val dataGenerateReporteSaldosPorCobrar = MutableLiveData<Boolean>()
    fun generateReporteSaldosPorCobrar(fechaInicio: String, fechaFin: String, context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReporteSaldosPorCobrarUseCase(fechaInicio, fechaFin, context)

            result.let {
                dataGenerateReporteSaldosPorCobrar.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Genera el REPORTE DE PRODUCTOS POR MARCA
    val dataGenerateReporteProductosPorMarca = MutableLiveData<Boolean>()
    fun generateReporteProductosPorMarca(context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReporteProductosPorMarcaUseCase(context)

            result.let {
                dataGenerateReporteProductosPorMarca.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Genera el REPORTE DE ESTADO DE CUENTA
    val dataGenerateReporteEstadoCuenta = MutableLiveData<Boolean>()

    fun generateReporteEstadoCuenta(cardCode: String, context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReporteEstadoCuentaUseCase(cardCode, context)

            result.let {
                dataGenerateReporteEstadoCuenta.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Genera el REPORTE DE PRE COBRANZA
    val dataGenerateReportePreCobranza = MutableLiveData<Boolean>()
    fun generateReportePreCobranza(fechaInicio: String, fechaFin: String, context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReportePreCobranzaUseCase(fechaInicio, fechaFin, context)

            result.let {
                dataGenerateReportePreCobranza.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Genera el REPORTE DE AVANCES DE VENTAS
    val dataGenerateReporteAvancesVentas = MutableLiveData<Boolean>()
    fun generateReporteAvancesVentas(fechaInicio: String, fechaFin: String, context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReporteAvancesVentasUseCase(fechaInicio, fechaFin, context)

            result.let {
                dataGenerateReporteAvancesVentas.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Genera el REPORTE DE VENTAS DIARIAS
    val dataGenerateReporteVentasDiarias = MutableLiveData<Boolean>()
    fun generateReporteVentasDiarias(fechaInicio: String, fechaFin: String, context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReporteVentasDiariasUseCase(fechaInicio, fechaFin, context)

            result.let {
                dataGenerateReporteVentasDiarias.postValue(result)
                isLoading.postValue(false)
            }
        }
    }

        //Genera el REPORTE DE DETALLE DE VENTAS
    val dataGenerateReporteDetalleVentas = MutableLiveData<Boolean>()
    fun generateReporteDetalleVentas(fechaInicio: String, fechaFin: String, context: Context){
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = generateReporteDetalleVentasUseCase(fechaInicio, fechaFin, context)

            result.let {
                dataGenerateReporteDetalleVentas.postValue(result)
                isLoading.postValue(false)
            }
        }
    }
}