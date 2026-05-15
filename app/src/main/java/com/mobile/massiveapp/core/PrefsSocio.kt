package com.mobile.massiveapp.core

import android.content.Context

class PrefsSocio (
    context: Context
) {
    val SHARED_NAME = "PrefsSocio"
    val SHARED_CARD_NAME = "cardName"
    val SHARED_CARD_CODE = "cardCode"
    val SHARED_ACCDOCENTRY = "accDocEntry"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveCardName(tipo: String){
        storage.edit().putString(SHARED_CARD_NAME, tipo).apply()
    }
    fun saveCardCode(tipo: String){
        storage.edit().putString(SHARED_CARD_CODE, tipo).apply()
    }

    fun saveAccDocEntry(accDocEntry: String){
        storage.edit().putString(SHARED_ACCDOCENTRY, accDocEntry).apply()
    }


    fun getCardName(): String{
        return storage.getString(SHARED_CARD_NAME, "")!!
    }
    fun getCardCode(): String{
        return storage.getString(SHARED_CARD_CODE, "")!!
    }
    fun getAccDocEntry():String{
        return storage.getString(SHARED_ACCDOCENTRY, "")!!
    }
    fun wipe(){
        storage.edit().clear().apply()
    }
}