package com.mobile.massiveapp.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobile.massiveapp.data.network.interceptor.ClientXmlInterceptor
import com.mobile.massiveapp.data.util.GetConfigInfoForCalls
import com.mobile.massiveapp.data.util.ToXmlSendRequestBody
import com.mobile.massiveapp.data.util.toXmlRequestBody
import com.mobile.massiveapp.domain.model.DoClienteRutaComercial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RutaComercialService @Inject constructor(
    private val getConfigInfoForCalls: GetConfigInfoForCalls
) {

    suspend fun obtenerClienteRutasComerciales(
        timeout: Long = 60L
    ): List<DoClienteRutaComercial> {
        return withContext(Dispatchers.IO) {
            val configInfo = getConfigInfoForCalls()
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(RutaComercialClient::class.java)
            val requestBody = "".toXmlRequestBody("ClienteRutasComerciales", configInfo.configuracion, configInfo.usuario)
            val response = apiService.obtenerClienteRutasComerciales(configInfo.url, requestBody)
            val result = response.body()?.body?.response?.result
            Timber.d("ObtenerClienteRutasComerciales code: ${response.code()}, error: ${result?.errorCodigo}, json: ${result?.json}")
            val json = result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<DoClienteRutaComercial>>() {}.type
            gson.fromJson(json, listType) ?: emptyList()
        }
    }

    suspend fun insertarClienteRutasComerciales(
        json: String,
        timeout: Long = 60L
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val configInfo = getConfigInfoForCalls()
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(RutaComercialClient::class.java)
            val requestBody = "".ToXmlSendRequestBody("ClienteRutasComerciales", json, configInfo.configuracion, configInfo.usuario)
            val response = apiService.insertarClienteRutasComerciales(configInfo.url, requestBody)
            val result = response.body()?.body?.response?.result
            Timber.d("InsertarClienteRutasComerciales code: ${response.code()}, error: ${result?.errorCodigo}, mensaje: ${result?.errorMensaje}")
            result?.errorCodigo == 0
        }
    }

    private fun createRetrofitClient(timeout: Long): Retrofit {
        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)
        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(ClientXmlInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl("http://your.api.base.url/")
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
            .client(client)
            .build()
    }
}
