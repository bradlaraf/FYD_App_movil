package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralMonedas(
    val CurrCode: String,
    val CurrName: String,
    val ISOCurrCod: String
): MappingInteface<GeneralMonedas>(){
    constructor(): this(
        CurrCode = "",
        CurrName = "",
        ISOCurrCod = ""
    )
    override fun map(data: List<GeneralMonedas>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralMonedas>): List<Any> {
        return data.map { it.CurrCode }
    }

}