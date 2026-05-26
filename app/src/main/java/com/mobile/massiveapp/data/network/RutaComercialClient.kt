package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.network.response.SoapEnvelopeInsertarClienteRutasComerciales
import com.mobile.massiveapp.data.network.response.SoapEnvelopeObtenerClienteRutasComerciales
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface RutaComercialClient {

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun obtenerClienteRutasComerciales(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeObtenerClienteRutasComerciales>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun insertarClienteRutasComerciales(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeInsertarClienteRutasComerciales>
}
