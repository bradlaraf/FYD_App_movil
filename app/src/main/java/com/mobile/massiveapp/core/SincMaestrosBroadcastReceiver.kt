package com.mobile.massiveapp.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


class SincMaestrosBroadcastReceiver: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, p1: Intent?) {
        val serviceIntent = Intent(context, ForegroundDatosMaestrosService::class.java)
        ContextCompat.startForegroundService(context!!, serviceIntent)
    }
}