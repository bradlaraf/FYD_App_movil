package com.mobile.massiveapp.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ClientXmlInterceptor() : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "text/xml")
            .build()

        return chain.proceed(request)
    }

}