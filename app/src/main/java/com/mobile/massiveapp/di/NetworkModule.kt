package com.mobile.massiveapp.di

import com.mobile.massiveapp.data.network.ApiClient
import com.mobile.massiveapp.data.network.ApiStandardClient
import com.mobile.massiveapp.data.network.interceptor.ClientXmlInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Named("BaseUrl")
    @Provides
    fun provideRetrofit(): Retrofit {
        val xmlConverterFactory = create()
        return Retrofit.Builder()
            .baseUrl("http://159.138.116.164:9110/")
            .addConverterFactory(xmlConverterFactory)
            .client(getClient())
            .build()
    }

    @Singleton
    @Named("ConsultaRucUrl")
    @Provides
    fun provideRetrofitConsulta(): Retrofit {
        val xmlConverterFactory = create()
        return Retrofit.Builder()
            .baseUrl("http://159.138.116.164:54982/")
            .addConverterFactory(xmlConverterFactory)
            .client(getClient())
            .build()
    }


    @Singleton
    @Provides
    fun getClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .addInterceptor(ClientXmlInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(80, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        return client
    }

    @Singleton
    @Provides
    fun provideApiConsultaRuc(@Named("ConsultaRucUrl") retrofit: Retrofit): ApiClient{
        return retrofit.create(ApiClient::class.java)
    }


    @Singleton
    @Provides
    fun provideApiClientXml(@Named("BaseUrl") retrofit: Retrofit): ApiStandardClient{
        return retrofit.create(ApiStandardClient::class.java)
    }

}