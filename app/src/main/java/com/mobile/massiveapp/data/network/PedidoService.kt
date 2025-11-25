package com.mobile.massiveapp.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.data.network.interceptor.ClientXmlInterceptor
import com.mobile.massiveapp.data.util.GetConfigInfoForCalls
import com.mobile.massiveapp.data.util.getXmlPedidoSugerido
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoUsuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PedidoService @Inject constructor(
    private val getConfigInfoForCalls: GetConfigInfoForCalls
){

    suspend fun getPedidoSugerido(
        cardCode: String,
        timeout: Long = 15L,
    ):List<ClientePedidoDetalle>{
        return withContext(Dispatchers.IO) {
            val configInfo = getConfigInfoForCalls()
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ReportesClient::class.java)

            // Crear el cuerpo de la solicitud XML
            val requestBody = getXmlPedidoSugerido("PedidoSugerido", configInfo.configuracion, configInfo.usuario, cardCode)

            val response = apiService.getReporteVentaDiaria(configInfo.url, requestBody)

            val soapBody = response.body()?.body

            val json = soapBody?.response?.result?.json
            val gson = Gson()
            val listType = object : TypeToken<List<ClientePedidoDetalle>>() {}.type
            gson.fromJson(json, listType)
        }
    }


    //Por mejorar
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