package com.mobile.massiveapp.data.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiClient {
    @POST("/WebS_CONSPE.asmx")
    suspend fun getConsultaRuc(
        @Body xmlRequestBody: RequestBody
    ): Response<ResponseBody>

    @POST
    suspend fun getConsultaDni(
        @Body xmlRequestBody: RequestBody,
        @Url url: String
    ): Response<ResponseBody>
}