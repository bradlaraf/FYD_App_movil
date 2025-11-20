package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.network.interceptor.ClientXmlInterceptor
import com.mobile.massiveapp.data.util.getXmlParaReporteConFecha
import com.mobile.massiveapp.data.util.getXmlParaReporteEstadoCuenta
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.domain.model.ReporteAvanceVentas
import com.mobile.massiveapp.domain.model.ReporteDetalleVenta
import com.mobile.massiveapp.domain.model.ReporteEstadoCuenta
import com.mobile.massiveapp.domain.model.ReportePreCobranza
import com.mobile.massiveapp.domain.model.ReporteSaldosPorCobrar
import com.mobile.massiveapp.domain.model.ReporteVentasDiarias
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReportesService @Inject constructor() {

    suspend fun getReporteVentaDiaria(
        fechaInicial: String,
        fechaFinal: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReporteVentasDiarias>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ReportesClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlParaReporteConFecha("ReporteVentaDiaria", configuracion, usuario, fechaInicial, fechaFinal)

            val response = apiService.getReporteVentaDiaria(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ReporteVentasDiarias>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getReporteAvanceVentas(
        fechaInicial: String,
        fechaFinal: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReporteAvanceVentas>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ReportesClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlParaReporteConFecha("ReporteAvanceVenta", configuracion, usuario, fechaInicial, fechaFinal)

            val response = apiService.getReporteAvanceVenta(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ReporteAvanceVentas>>() {}.type
            gson.fromJson(json, listType)
        }
    }


    suspend fun getReportePreCobranza(
        fechaInicial: String,
        fechaFinal: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReportePreCobranza>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ReportesClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlParaReporteConFecha("ReportePreCobranza", configuracion, usuario, fechaInicial, fechaFinal)

            val response = apiService.getReportePreCobranza(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ReportePreCobranza>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getReporteSaldosPorCobrar(
        fechaInicial: String,
        fechaFinal: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReporteSaldosPorCobrar>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ReportesClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlParaReporteConFecha("ReporteCuentaCobrar", configuracion, usuario, fechaInicial, fechaFinal)

            val response = apiService.getReporteSaldosPorCobrar(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ReporteSaldosPorCobrar>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    suspend fun getReporteEstadoCuenta(
        codigoCliente: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReporteEstadoCuenta>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ReportesClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlParaReporteEstadoCuenta("ReporteEstadoCuenta", configuracion, usuario,codigoCliente)

            val response = apiService.getReporteEstadoCuenta(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ReporteEstadoCuenta>>() {}.type
            gson.fromJson(json, listType)
        }
    }

    /*ReporteDetalleVenta*/

    suspend fun getReporteDetalleVenta(
        fechaInicial: String,
        fechaFinal: String,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L,
    ):List<ReporteDetalleVenta>{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ReportesClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlParaReporteConFecha("ReporteDetalleVenta", configuracion, usuario, fechaInicial, fechaFinal)

            val response = apiService.getReporteDetalleVenta(url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ReporteDetalleVenta>>() {}.type
            gson.fromJson(json, listType)
        }
    }



    fun createRetrofitClient(timeout: Long): Retrofit {
        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)

        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(ClientXmlInterceptor().apply {})
            .build()

        return Retrofit.Builder()
            .baseUrl("http://your.api.base.url/")
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
            .client(client)
            .build()
    }
}