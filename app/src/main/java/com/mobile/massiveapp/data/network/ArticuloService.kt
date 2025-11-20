package com.mobile.massiveapp.data.network

import com.mobile.massiveapp.data.model.Articulo
import com.mobile.massiveapp.data.util.parseXmlAndGetJsonValue
import com.mobile.massiveapp.data.util.toXmlRequestBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticuloService @Inject constructor(
        private val api: ApiStandardClient
) {
    suspend fun getArticulos():List<Articulo> {
        return withContext(Dispatchers.IO){
            val endPoint = "".toXmlRequestBody("Articulo")
            val response = api.getData(endPoint, "")
            val responseBody = response.body()?.string()

            val json = responseBody?.parseXmlAndGetJsonValue() ?: throw NullPointerException("Response is null")

            val gson = Gson()
            val articulosList: List<Articulo> = gson.fromJson(json, object : TypeToken<List<Articulo>>() {}.type) ?: emptyList()
            articulosList
        }
    }

    /*suspend fun getArticuloPrecios(): List<ArticuloPrecio> {
        return withContext(Dispatchers.IO){
            //Llamada a la API con el endpoint
            val response: Response<ResponseBody> = api.getData("".toXmlRequestBody("ArticuloPrecio"))
            //Obtener el body de la respuesta
            val responseBody = response.body()?.string()
            //Parsear el body a JSON
            val json = responseBody?.parseXmlAndGetJsonValue() ?: throw NullPointerException("Response is null")
            //Convertir el JSON a una lista de ArticuloPrecio
            val articuloPrecios: List<ArticuloPrecio> = Gson().fromJson(json, object : com.google.gson.reflect.TypeToken<List<ArticuloPrecio>>() {}.type) ?: emptyList()
            articuloPrecios
        }
    }

    suspend fun getArticuloListaPrecios(): List<ArticuloListaPrecios> {
        return withContext(Dispatchers.IO){
            val response: Response<ResponseBody> = api.getData("".toXmlRequestBody("ArticuloListaPrecios"))
            val responseBody = response.body()?.string()

            val json = responseBody?.parseXmlAndGetJsonValue() ?: throw NullPointerException("Response is null")
            val articuloListaPrecios: List<ArticuloListaPrecios> = Gson().fromJson(json, object : com.google.gson.reflect.TypeToken<List<ArticuloListaPrecios>>() {}.type) ?: emptyList()
            articuloListaPrecios
        }
    }*/



}