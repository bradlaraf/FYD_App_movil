package com.mobile.massiveapp.core

import android.content.Context
import com.mobile.massiveapp.ui.view.util.getFechaActual

class PrefsManifiesto(context: Context) {
    val SHARED_NAME = "PrefsManifiesto"
    val SHARED_FECHA_INICIO = "fechaInicio"
    val SHARED_FECHA_FIN = "fechaFin"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveFechaInicio(fechaInicio: String) {
        storage.edit().putString(SHARED_FECHA_INICIO, fechaInicio).apply()
    }

    fun saveFechaFin(fechaFin: String) {
        storage.edit().putString(SHARED_FECHA_FIN, fechaFin).apply()
    }

    fun getFechaInicio(): String {
        return storage.getString(SHARED_FECHA_INICIO, "2000-01-01")!!
    }

    fun getFechaFin(): String {
        return storage.getString(SHARED_FECHA_FIN, getFechaActual())!!
    }
}
