package com.mobile.massiveapp.di

import android.content.Context
import com.mobile.massiveapp.core.ConnectivityObserver
import com.mobile.massiveapp.core.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkConnectivityObserver {

    @Provides
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }


}