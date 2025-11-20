package com.mobile.massiveapp.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class TaskReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Timber.tag("taskReceiver").d("Hola mundo")

    }
}