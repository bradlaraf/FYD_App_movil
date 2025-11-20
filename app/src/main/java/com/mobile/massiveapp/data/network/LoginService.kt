package com.mobile.massiveapp.data.network

import android.content.Context
import com.mobile.massiveapp.data.model.Usuario
import com.mobile.massiveapp.data.network.interceptor.ClientXmlInterceptor
import com.mobile.massiveapp.data.util.getXmlCerrarSesionRequestBody
import com.mobile.massiveapp.data.util.getXmlLoginRequestBody
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoError
import com.mobile.massiveapp.domain.model.DoUsuario
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

class LoginService @Inject constructor(
    private val api: ApiStandardClient,
    private val client: OkHttpClient
) {
    suspend fun login (
        version: String,
        usuario:String,
        password:String,
        context: Context,
        configuracion: DoConfiguracion,
        url: String
    ): Any{
         return withContext(Dispatchers.IO){
             val retrofit = createRetrofitClient(timeout = 15L)
             val apiService = retrofit.create(ApiStandardClient::class.java)

             val requestBody = getXmlLoginRequestBody(version, usuario, password, configuracion)
             val response = apiService.login(requestBody, url)
             val soapBody = response.body()?.body

             val codigoError = soapBody?.response?.result?.errorCodigo
             val codigoMensaje = soapBody?.response?.result?.errorMensaje


             val json = soapBody?.response?.result?.json

             val gson = Gson()
             val objType = object : TypeToken<Usuario>() {}.type


             if ((codigoError ?: -1) == 0){
                 return@withContext gson.fromJson(json, objType)
             } else {
                 DoError(
                     ErrorCodigo = codigoError?:-1,
                     ErrorMensaje = codigoMensaje?:"No se pudo conectar a la BD"
                 )
             }
        }
    }

    suspend fun cerrarSesion(
        usuario: DoUsuario,
        configuracion: DoConfiguracion,
        url: String,
        timeout: Long = 60L
    ): DoError{
        return withContext(Dispatchers.IO) {
            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            val requestBody = getXmlCerrarSesionRequestBody(usuario, configuracion)
            val response = apiService.cerrarSesion(url, requestBody)
            val soapBody = response.body()?.body

            val codigoError = soapBody?.response?.result?.errorCodigo
            val codigoMensaje = soapBody?.response?.result?.errorMensaje

            DoError(
                ErrorCodigo = codigoError?:-1,
                ErrorMensaje = codigoMensaje?:"No se pudo realizar el cierre de sesión"
            )
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