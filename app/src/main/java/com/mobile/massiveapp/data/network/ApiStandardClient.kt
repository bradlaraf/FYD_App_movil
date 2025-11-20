package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.network.response.SoapEnvelope
import com.mobile.massiveapp.data.network.response.SoapEnvelopeArticuloCantidades
import com.mobile.massiveapp.data.network.response.SoapEnvelopeArticuloPrecios
import com.mobile.massiveapp.data.network.response.SoapEnvelopeArticulos
import com.mobile.massiveapp.data.network.response.SoapEnvelopeCerrarSesion
import com.mobile.massiveapp.data.network.response.SoapEnvelopeClienteContactos
import com.mobile.massiveapp.data.network.response.SoapEnvelopeClienteDirecciones
import com.mobile.massiveapp.data.network.response.SoapEnvelopeClientes
import com.mobile.massiveapp.data.network.response.SoapEnvelopeEstadoSesion
import com.mobile.massiveapp.data.network.response.SoapEnvelopeFacturas
import com.mobile.massiveapp.data.network.response.SoapEnvelopeFacturasDetalle
import com.mobile.massiveapp.data.network.response.SoapEnvelopeInsertarClientes
import com.mobile.massiveapp.data.network.response.SoapEnvelopeInsertarUsuario
import com.mobile.massiveapp.data.network.response.SoapEnvelopeUsuario
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiStandardClient {
    @POST
    suspend fun getData(
        @Body xmlRequestBody: RequestBody,
        @Url url: String
    ): Response<ResponseBody>

    @POST
    suspend fun sendDatosMaestros(
        @Body xmlRequestBody: RequestBody,
        @Url url: String
    ): Response<ResponseBody>

    @POST
    suspend fun sendUsuarios(
        @Body xmlRequestBody: RequestBody,
        @Url url: String
    ): Response<SoapEnvelopeInsertarUsuario>


    @POST
    @Headers("Content-Type: text/xml")
    suspend fun cerrarSesion(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeCerrarSesion>

    @POST
    suspend fun login(
        @Body xmlRequestBody: RequestBody,
        @Url url: String
    ): Response<SoapEnvelopeUsuario>



    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getDatoMaestro(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelope>


    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getClientes(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeClientes>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun sendClientes(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeInsertarClientes>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getClienteContactos(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeClienteContactos>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getClienteDirecciones(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeClienteDirecciones>


    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getArticulos(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeArticulos>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getArticulosPrecios(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeArticuloPrecios>

    @POST
    @Headers("Content-Type: text/xml")
    suspend fun getArticulosCantidades(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeArticuloCantidades>


    @POST
    @Headers ("Content-Type: text/xml")
    suspend fun getFacturas(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeFacturas>

    @POST
    @Headers ("Content-Type: text/xml")
    suspend fun getFacturasDetalle(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeFacturasDetalle>

    @POST
    @Headers ("Content-Type: text/xml")
    suspend fun getEstadoSesion(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response<SoapEnvelopeEstadoSesion>

}
