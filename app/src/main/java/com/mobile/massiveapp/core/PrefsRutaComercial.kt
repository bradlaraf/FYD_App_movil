package com.mobile.massiveapp.core

import android.content.Context

class PrefsRutaComercial (
    context: Context
) {
    val SHARED_NAME = "PrefsRutaComercial"
    val SHARED_ACCDOCENTRY = "accDocEntry"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveAccDocEntry(accDocEntry: String){
        storage.edit().putString(SHARED_ACCDOCENTRY, accDocEntry).apply()
    }

    fun getAccDocEntry():String{
        return storage.getString(SHARED_ACCDOCENTRY, "")!!
    }
    fun wipe(){
        storage.edit().clear().apply()
    }
}