package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.network.response.SoapEnvelopeReporteAvanceVenta
import com.mobile.massiveapp.data.network.response.SoapEnvelopeReporteCuentaCobrar
import com.mobile.massiveapp.data.network.response.SoapEnvelopeReporteDetalleVenta
import com.mobile.massiveapp.data.network.response.SoapEnvelopeReporteEstadoCuenta
import com.mobile.massiveapp.data.network.response.SoapEnvelopeReportePreCobranza
import com.mobile.massiveapp.data.network.response.SoapEnvelopeReporteVentaDiaria
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ReportesClient {

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getReporteVentaDiaria(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeReporteVentaDiaria>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getReporteAvanceVenta(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeReporteAvanceVenta>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getReportePreCobranza(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeReportePreCobranza>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getReporteDetalleVenta(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeReporteDetalleVenta>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getReporteEstadoCuenta(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeReporteEstadoCuenta>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getReporteSaldosPorCobrar(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeReporteCuentaCobrar>
}