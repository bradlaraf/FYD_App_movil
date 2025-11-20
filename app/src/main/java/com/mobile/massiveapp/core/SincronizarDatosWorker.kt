package com.mobile.massiveapp.core

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mobile.massiveapp.R
import com.mobile.massiveapp.core.SincronizarDatosWorker.Companion.NOTIFICATION_ID
import com.mobile.massiveapp.domain.GetDatosMaestrosUseCase
import com.mobile.massiveapp.domain.SendDatosMaestrosUseCase
import com.mobile.massiveapp.domain.configuracion.GetConfiguracionActualUseCase
import com.mobile.massiveapp.ui.view.menu.MenuActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SincronizarDatosWorker @AssistedInject constructor (
    @Assisted private val sincronizarDatosMaestrosUseCase: GetDatosMaestrosUseCase,
    @Assisted private val sendDatosMaestrosUseCase: SendDatosMaestrosUseCase,
    @Assisted private val getConfiguracionActualUseCase: GetConfiguracionActualUseCase,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    companion object{
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
    }
    override suspend fun doWork(): Result {
        return try {
            val configuracion = getConfiguracionActualUseCase()
            var sincronizacionExitosa = false


            configuracion.let {
                if (it.SincAutomatica){
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        sincronizacionExitosa = (sendDatosMaestrosUseCase(){progress, message, maxLenght->}.ErrorCodigo == 0) and (sincronizarDatosMaestrosUseCase {progress, message, maxLenght-> }.ErrorCodigo == 0)
                        /*notificationTest("Sincronización exitosa...")
                        cancelNotification(applicationContext)*/
                    } else {
                        sincronizacionExitosa = (sendDatosMaestrosUseCase(){progress, message, maxLenght->
                        }.ErrorCodigo == 0) and (sincronizarDatosMaestrosUseCase {progress, message, maxLenght-> }.ErrorCodigo == 0)
                        /*notificationTest("Sincronización exitosa...")
                        cancelNotification(applicationContext)*/
                    }
                }
            }

            return if (sincronizacionExitosa) Result.success()
            else Result.retry()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().log("${e.message}")
            Result.failure()
        } finally {
            cancelNotification(applicationContext)
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(applicationContext)
    }

    @SuppressLint("MissingPermission")
    private fun notificationTest(message: String){
        val intent = Intent(applicationContext, MenuActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.icon_sincronizar)
            .setContentTitle("Sincronizacion de datos")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)){
            notify(NOTIFICATION_ID, notification.build())
        }

    }

}

private fun cancelNotification(context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(NOTIFICATION_ID) // Cancela la notificación con el ID 1
}


/*private fun updateNotification(context: Context, progress: Int, message: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val updatedBuilder = NotificationCompat.Builder(context, "main_channel_id")
        .setSmallIcon(R.drawable.icon_actividades)
        .setContentTitle("Sincronizando datos maestros")
        .setContentText("Progreso: $progress% - $message")
        .setOngoing(true)
        .setAutoCancel(true)
        .setProgress(100, progress, false)

    notificationManager.notify(1, updatedBuilder.build())
}*/
private fun sendNotificationBroadcast(context: Context, progress: Int, message: String, maxLength: Int) {
    val intent = Intent(context, SincMaestrosBroadcastReceiver::class.java).apply {
        action = Intent.ACTION_BOOT_COMPLETED
        putExtra("progress", progress)
        putExtra("message", message)
        putExtra("maxLength", maxLength)
    }
    context.sendBroadcast(intent)
}

private fun showSimpleNotification(context: Context, message: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Cancelar la notificación existente
    notificationManager.cancel(1)

    // Construir una nueva notificación
    val notification = NotificationCompat.Builder(context, "main_channel_id")
        .setSmallIcon(R.drawable.icon_sincronizar)
        .setContentTitle("Sincronizando datos maestros")
        .setContentText(message)
        .setAutoCancel(true)
        .build()

    // Mostrar la nueva notificación
    notificationManager.notify(1, notification)
}


private fun updateNotification(context: Context, progress: Int, message: String, maxLength: Int) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Cancelar la notificación existente
    notificationManager.cancel(1)

    // Construir una nueva notificación
    val notification = NotificationCompat.Builder(context, "main_channel_id")
        .setSmallIcon(R.drawable.icon_sincronizar)
        .setContentTitle("$message")
        .setOngoing(true)
        .setAutoCancel(true)
        .setProgress(maxLength, progress, false)
        .build()

    // Mostrar la nueva notificación
    notificationManager.notify(1, notification)
}




private fun getForegroundInfo(context: Context): ForegroundInfo{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ForegroundInfo(
            1,
            createNotification(context = context),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
        )
    } else {
        ForegroundInfo(
            1,
            createNotification(context = context)
        )
    }
}




private fun createNotification(context: Context): Notification{
    val CHANNEL_ID = "main_channel_id"
    val CHANNEL_NAME = "Main channel"

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.icon_sincronizar)
        .setContentTitle("Sincronizando datos maestros")
        .setContentText("Descargando contenido...")
        .setOngoing(true)
        .setAutoCancel(true)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }
    return builder.build()
}

