package com.mobile.massiveapp.core

import android.content.Context

class Prefs(
    context: Context
) {
    val SHARED_NAME = "Prefs"
    val SHARED_TIPO_PAGO = "tipoPago"
    val SHARED_CUENTA_PAGO = "cuentaPago"
    val SHARED_CHEQ_NUM = "chequeNumero"
    val SHARED_CHEQ_DATE = "chequeFecha"
    val SHARED_CHEQ_BANK = "chequeBanco"
    val SHARED_TRANS_REF = "transRef"

    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveTipoPago(tipo: String){
        storage.edit().putString(SHARED_TIPO_PAGO, tipo).apply()
    }

    fun saveCuentaPago(tipo:String){
        storage.edit().putString(SHARED_CUENTA_PAGO, tipo).apply()
    }

    fun saveNumeroCheque(numero: Int){
        storage.edit().putInt(SHARED_CHEQ_NUM, numero).apply()
    }

    fun saveFechaCheque(fecha: String){
        storage.edit().putString(SHARED_CHEQ_DATE, fecha).apply()
    }

    fun saveBancoCheque(banco: String){
        storage.edit().putString(SHARED_CHEQ_BANK, banco).apply()
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

    fun getNumeroCheque(): Int{
        return storage.getInt(SHARED_CHEQ_NUM, -1)
    }

    fun getFechaCheque(): String{
        return storage.getString(SHARED_CHEQ_DATE, "")!!
    }

    fun getBancoCheque(): String{
        return storage.getString(SHARED_CHEQ_BANK, "")!!
    }

    fun getReferenciaTransfer(): String{
        return storage.getString(SHARED_TRANS_REF, "")!!
    }

    fun wipe(){
        storage.edit().clear().apply()
    }
}