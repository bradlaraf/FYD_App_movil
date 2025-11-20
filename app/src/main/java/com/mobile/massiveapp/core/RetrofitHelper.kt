package com.mobile.massiveapp.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://bradlara.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}