package com.mobile.massiveapp.core

import android.content.Context

class PrefsApp (
    context: Context
) {
    val SHARED_NAME = "PrefsApp"
    val SHARED_VERSION_APP = "versionApp"
    val SHARED_USER_NAME = "userName"
    val SHARED_CHANGE_HAPPENED = "change_happened"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveVersionApp(tipo: String){
        storage.edit().putString(SHARED_VERSION_APP, tipo).apply()
    }

    fun saveUserName(userName: String){
        storage.edit().putString(SHARED_USER_NAME, userName).apply()
    }

    fun saveChangeHappened(change: String){
        storage.edit().putString(SHARED_CHANGE_HAPPENED, change).apply()
    }


    fun getVersionApp(): String{
        return storage.getString(SHARED_VERSION_APP, "")!!
    }

    fun getUserName(): String{
        return storage.getString(SHARED_USER_NAME, "")!!
    }

    fun getChangeHappened():String {
        return storage.getString(SHARED_CHANGE_HAPPENED, "")!!
    }

    fun wipe(){
        storage.edit().clear().apply()
    }
}