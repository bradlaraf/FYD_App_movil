package com.mobile.massiveapp.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.SendDatosMaestrosUseCase
import com.mobile.massiveapp.domain.configuracion.GetConfiguracionActualUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SincDocumentosWorker  @AssistedInject constructor (
    @Assisted private val sincronizarDocumentos: SendDatosMaestrosUseCase,
    @Assisted private val getConfiguracionActualUseCase: GetConfiguracionActualUseCase,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val configuracion = getConfiguracionActualUseCase()
            var sincronizacionExitosa = false


            configuracion.let {
                if (it.SincAutomatica){
                    sincronizacionExitosa = (sincronizarDocumentos() {progress, message, maxLenght->
                        updateNotification(applicationContext, progress, message, maxLenght)
                    }.ErrorCodigo == 0)
                }
            }
            return if (sincronizacionExitosa) Result.success()
            else Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(applicationContext)
    }

}


private fun updateNotification(context: Context, progress: Int, message: String, maxLength: Int) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Cancelar la notificación existente
    notificationManager.cancel(1)

    // Construir una nueva notificación
    val notification = NotificationCompat.Builder(context, "main_channel_documentos_id")
        .setSmallIcon(R.drawable.icon_sincronizar)
        .setContentTitle("$message")
        .setOngoing(true)
        .setAutoCancel(true)
        .setProgress(maxLength, progress, false)
        .build()

    // Mostrar la nueva notificación
    notificationManager.notify(22, notification)
}




private fun getForegroundInfo(context: Context): ForegroundInfo {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ForegroundInfo(
            22,
            createNotification(context = context),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
        )
    } else {
        ForegroundInfo(
            22,
            createNotification(context = context)
        )
    }
}




private fun createNotification(context: Context): Notification {
    val CHANNEL_ID = "main_channel_documentos_id"
    val CHANNEL_NAME = "Sinc Documentos"

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.icon_arroy_enter)
        .setContentTitle("Sincronizando documentos")
        .setContentText("Enviando contenido...")
        .setOngoing(true)
        .setAutoCancel(true)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
    return builder.build()
}