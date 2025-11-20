package com.mobile.massiveapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mobile.massiveapp.core.Prefs
import com.mobile.massiveapp.core.PrefsApp
import com.mobile.massiveapp.core.PrefsPedido
import com.mobile.massiveapp.core.SincronizarDatosWorker
import com.mobile.massiveapp.domain.GetDatosMaestrosUseCase
import com.mobile.massiveapp.domain.SendDatosMaestrosUseCase
import com.mobile.massiveapp.domain.configuracion.GetConfiguracionActualUseCase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MassiveApp:Application(), Configuration.Provider {

    companion object{
        lateinit var prefs: Prefs
        lateinit var prefsPedido: PrefsPedido
        lateinit var prefsApp: PrefsApp
    }

    @Inject
    lateinit var  workerFactory: CustomWorkerFactory
    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        prefsPedido = PrefsPedido(applicationContext)
        prefsApp = PrefsApp(applicationContext)

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "666",
                "Foreground Notification",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        Timber.plant(Timber.DebugTree())


    }


    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}


class CustomWorkerFactory @Inject constructor(
    private val getDatosMaestrosUseCase: GetDatosMaestrosUseCase,
    private val getConfiguracionActualUseCase: GetConfiguracionActualUseCase,
    private val sendDatosMaestrosUseCase: SendDatosMaestrosUseCase,
): WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = SincronizarDatosWorker(getDatosMaestrosUseCase, sendDatosMaestrosUseCase,getConfiguracionActualUseCase,appContext, workerParameters)

}