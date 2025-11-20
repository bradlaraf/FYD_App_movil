package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.database.dao.ErrorLogDao
import com.mobile.massiveapp.data.model.UsuarioToSend
import com.mobile.massiveapp.data.network.interceptor.ClientXmlInterceptor
import com.mobile.massiveapp.data.util.ToXmlSendRequestBody
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

class UsuariosService @Inject constructor(
    private val api: ApiStandardClient,
    private val client: OkHttpClient,
    private val errorLogDao: ErrorLogDao
) {

    suspend fun sendUsuario(
        usuarioToSend: List<UsuarioToSend>,
        configuracion: DoConfiguracion,
        usuario: DoUsuario,
        url: String,
        timeout: Long = 60L
    ): Any{
        return withContext(Dispatchers.IO){
            val gson = Gson()
            val listaDatosJsonArray = gson.toJson(usuarioToSend).replace("\n", " ")

            val retrofit = createRetrofitClient(timeout)
            val apiService = retrofit.create(ApiStandardClient::class.java)

            val requestBody = "".ToXmlSendRequestBody("Usuarios", listaDatosJsonArray, configuracion, usuario)
            val response = apiService.sendUsuarios(requestBody, url)
            val soapBody = response.body()?.body

            val codigoError = soapBody?.response?.result?.errorCodigo?:-1
            val codigoMensaje = soapBody?.response?.result?.errorMensaje

            val json = soapBody?.response?.result?.json

            val objType = object : TypeToken<List<UsuarioToSend>>() {}.type
            val listaUsuarios = gson.fromJson<List<UsuarioToSend>>(json, objType)?: emptyList()

            when(codigoError){
                -2 -> {
                    val messageError = if (listaUsuarios.isNotEmpty()){
                        listaUsuarios.first().AccError
                    } else {
                        "No se pudo conectar a la BD"
                    }
                    DoError(
                        ErrorCodigo = codigoError,
                        ErrorMensaje = messageError
                    )
                }

                0 -> {
                    return@withContext gson.fromJson(json, objType)
                }

                else -> {
                    DoError(
                        ErrorCodigo = codigoError,
                        ErrorMensaje = codigoMensaje?:"No se pudo conectar a la BD"
                    )
                }
            }
        }
    }

    fun createRetrofitClient(timeout: Long): Retrofit {
        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)

        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(ClientXmlInterceptor().apply {})
            .build()

        return Retrofit.Builder()
            .baseUrl("http://your.api.base.url/")
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
            .client(client)
            .build()
    }
}