package com.mobile.massiveapp.core

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackgroundService: Service() {
    private lateinit var connectivityObserver: ConnectivityObserver
    private val alarmManager by lazy { getSystemService(ALARM_SERVICE) as AlarmManager }

    override fun onTaskRemoved(rootIntent: Intent?) {

        super.onTaskRemoved(rootIntent)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() ->{
                start()
                scheduleTask()
            }

            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun scheduleTask() {
        val alarmIntent = Intent(this, TaskReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val interval = 5 * 60 * 1000 // Intervalo de 5 minutos en milisegundos

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            interval.toLong(),
            pendingIntent
        )
    }

    private fun start(){
        connectivityObserver = NetworkConnectivityObserver(this)
        observeConnectivity()
    }

    enum class Actions {
        START,
        STOP
    }

    private fun observeConnectivity() {
        val connectivityFlow = connectivityObserver.observe()
        CoroutineScope(Dispatchers.IO).launch {
            connectivityFlow.collect { status ->
                when (status) {
                    ConnectivityObserver.Status.Available -> {
                        Log.d("BackgroundService", "Conectado") // Mensaje de registro en la consola
                    }
                    ConnectivityObserver.Status.Unavailable -> {
                        Log.d("BackgroundService", "Desconectado")
                    }
                    else -> {
                        Log.d("BackgroundService", "Desconectado")
                    }
                }
            }
        }
    }



    override fun onBind(p0: Intent?): IBinder? {
            return null
        }
    }
