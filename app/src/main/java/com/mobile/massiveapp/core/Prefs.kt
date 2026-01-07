package com.mobile.massiveapp.core

import android.content.Context

class Prefs(
    context: Context
) {
    val SHARED_NAME = "Prefs"
    val SHARED_TIPO_PAGO = "tipoPago"
    val SHARED_TRANS_REF = "transRef"
    val SHARED_CUENTA_PAGO = "cuentaPago"


    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveTipoPago(tipo: String){
        storage.edit().putString(SHARED_TIPO_PAGO, tipo).apply()
    }

    fun saveCuentaPago(tipo:String){
        storage.edit().putString(SHARED_CUENTA_PAGO, tipo).apply()
    }

    fun saveReferenciaTransfer(numeroRef: String){
        storage.edit().putString(SHARED_TRANS_REF, numeroRef).apply()
    }


    fun getTipoPago(): String{
        return storage.getString(SHARED_TIPO_PAGO, "")!!
    }

    fun getCuentaPago(): String{
        return storage.getString(SHARED_CUENTA_PAGO, "")!!
    }


    fun getReferenciaTransfer(): String{
        return storage.getString(SHARED_TRANS_REF, "")!!
    }

    fun wipe(){
        storage.edit().clear().apply()
    }
}