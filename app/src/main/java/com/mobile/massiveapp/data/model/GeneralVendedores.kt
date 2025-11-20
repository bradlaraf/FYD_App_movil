package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralVendedores(
    val SlpCode: Int,
    val SlpName: String
): MappingInteface<GeneralVendedores>(){
    constructor(): this(
        0,
        "")

    override fun map(data: List<GeneralVendedores>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralVendedores>): List<Any> {
        return data.map { it.SlpCode }
    }
}