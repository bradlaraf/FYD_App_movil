package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.model.ConsultaDocumento
import com.mobile.massiveapp.data.model.SocioDirecciones
import com.mobile.massiveapp.data.util.getXmlForConsultaRucRequestBody
import com.mobile.massiveapp.data.util.parseXmlAndGetJsonValue
import com.mobile.massiveapp.data.util.parseXmlAndGetJsonValueForConsultaRuc
import com.mobile.massiveapp.data.util.toXmlRequestBody
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class SocioService @Inject constructor(
    private val apiConsultaRuc: ApiClient,
    private val apiStandardClient: ApiStandardClient
) {

        //Consulta de RUC
    suspend fun getConsultaRuc(
            configuracion: DoConfiguracion,
            tipoDocumento: String,
            numeroDocumento: String
        ): ConsultaDocumento =
        withContext(Dispatchers.IO) {
            try {
                val response = apiConsultaRuc.getConsultaRuc(getXmlForConsultaRucRequestBody(configuracion = configuracion, numeroDocumento = numeroDocumento, tipoDocumento = tipoDocumento))
                val responseBody = response.body()?.string()
                val json = responseBody?.parseXmlAndGetJsonValueForConsultaRuc(tipoDocumento)
                val gson = Gson()
                Timber.tag("ConsultaRuc").d("Response: ${
                    gson.fromJson(
                        json,
                        ConsultaDocumento::class.java
                    )
                }")
                gson.fromJson(json, ConsultaDocumento::class.java) ?: throw Exception("Error en en la conversion a JSON")

            } catch (e: Exception) {
                println(e)
                ConsultaDocumento()
            }
        }









    suspend fun getSocioDirecciones(): List<SocioDirecciones> {
        return withContext(Dispatchers.IO){
            val response: Response<ResponseBody> = apiStandardClient.getData("".toXmlRequestBody("SocioDirecciones"), "")
            val responseBody = response.body()?.string()

            val json = responseBody?.parseXmlAndGetJsonValue() ?: throw NullPointerException("Response is null")
            val sociosList: List<SocioDirecciones> = Gson().fromJson(json, object : com.google.gson.reflect.TypeToken<List<SocioDirecciones>>() {}.type) ?: emptyList()
            sociosList
        }
    }


}