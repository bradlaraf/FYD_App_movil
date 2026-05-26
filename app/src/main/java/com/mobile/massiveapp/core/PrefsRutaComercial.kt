package com.mobile.massiveapp.core

import android.content.Context
import com.mobile.massiveapp.ui.view.util.getFechaActual

class PrefsRutaComercial (
    context: Context
) {
    val SHARED_NAME = "PrefsRutaComercial"
    val SHARED_ACCDOCENTRY = "accDocEntry"
    val SHARED_FECHA_INICIO = "fechaInicio"
    val SHARED_FECHA_FIN = "fechaFin"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveAccDocEntry(accDocEntry: String){
        storage.edit().putString(SHARED_ACCDOCENTRY, accDocEntry).apply()
    }

    fun saveFechaInicio(fechaInicio: String){
        storage.edit().putString(SHARED_FECHA_INICIO, fechaInicio).apply()
    }

    fun saveFechaFin(fechaFin: String){
        storage.edit().putString(SHARED_FECHA_FIN, fechaFin).apply()
    }

    fun getAccDocEntry():String{
        return storage.getString(SHARED_ACCDOCENTRY, "")!!
    }

    fun getFechaInicio():String{
        return storage.getString(SHARED_FECHA_INICIO, getFechaActual())!!
    }

    fun getFechaFin():String{
        return storage.getString(SHARED_FECHA_FIN, getFechaActual())!!
    }
    fun wipe(){
        storage.edit().clear().apply()
    }
}