package com.mobile.massiveapp.core

import android.content.Context

class PrefsPedido(
    context: Context
) {
    val SHARED_NAME = "PrefsPedido"
    val SHARED_CARD_NAME = "cardName"
    val SHARED_CARD_CODE = "cardCode"
    val SHARED_ACCDOCENTRY = "accDocEntry"
    val SHARED_MAXIMO_LINEAS = "maximoLineas"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveCardName(tipo: String){
        storage.edit().putString(SHARED_CARD_NAME, tipo).apply()
    }
    fun saveCardCode(tipo: String){
        storage.edit().putString(SHARED_CARD_CODE, tipo).apply()
    }

    fun saveMaximoLineas(numero: Int){
        storage.edit().putInt(SHARED_MAXIMO_LINEAS, numero).apply()
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
    fun getMaximoLineas(): Int{
        return storage.getInt(SHARED_MAXIMO_LINEAS, 25)
    }
    fun getAccDocEntry():String{
        return storage.getString(SHARED_ACCDOCENTRY, "")!!
    }

    fun wipe(){
        storage.edit().clear().apply()
    }
}