package com.mobile.massiveapp.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.mobile.massiveapp.R
import com.mobile.massiveapp.domain.GetDatosMaestrosUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundDatosMaestrosService: Service() {
    @Inject
    lateinit var getDatosMaestrosUseCase: GetDatosMaestrosUseCase
    private var context: Context? = null
    private val NOTIFICATION_ID = 2
    private val CHANNEL_ID = "666"
    private var isDestroyec = false


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        startForeground(NOTIFICATION_ID, showNotification("Sincronizacion..."))

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch{
            executeUseCase()
        }
        return START_STICKY

    }

    /*private suspend fun executeUseCase() {
        getDatosMaestrosUseCase { progress, message, maxLenght ->
            sendNotificationBroadcast(this ,progress, message, maxLenght)
        }
    }*/
    private suspend fun executeUseCase() {
        getDatosMaestrosUseCase { progress, message, maxLength ->
            updateNotification(progress, message, maxLength)
            /*sendNotificationBroadcast(this ,progress, message, maxLength)*/
        }
        cancelNotification()
    }

    private fun sendNotificationBroadcast(context: Context, progress: Int, message: String, maxLength: Int) {
        val intent = Intent(context, SincMaestrosBroadcastReceiver::class.java).apply {
            action = Intent.ACTION_BOOT_COMPLETED
            putExtra("progress", progress)
            putExtra("message", message)
            putExtra("maxLength", maxLength)
        }
        context.sendBroadcast(intent)
    }

    private fun doTask(){
        val data = IntArray(1)
        val executorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executorService.execute{
            for (i in 0..99){
                if (isDestroyec){
                    break
                }

                data[0] = i
                try {
                    handler.post{
                        /*updateNotification(data[0].toString())*/

                    }
                    Thread.sleep(5000)
                } catch (e: InterruptedException){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateNotification(data: String, progress: String){
        val notification: Notification = showNotification(data, progress)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun updateNotification(progress: Int, message: String, maxLength: Int) {
        val notificationManager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)

        // Cancelar la notificación existente
        notificationManager.cancel(NOTIFICATION_ID)

        // Construir una nueva notificación
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_sincronizar)
            .setContentTitle("Sincronización Datos Maestros")
            .setContentText("Cargando...")
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        // Mostrar la nueva notificación
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun cancelNotification() {
        stopForeground(STOP_FOREGROUND_DETACH)
    }



    private fun showNotification(content: String, progress: String = ""): Notification{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID, "Foreground Notification",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sincronizando Datos")
            .setContentText("Obteniendo data...")
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.icon_sincronizar)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelNotification()
        isDestroyec = true
        Toast.makeText(context, "Stopping Service...", Toast.LENGTH_SHORT).show()
    }
}