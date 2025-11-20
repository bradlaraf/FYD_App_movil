package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Banco(
    val AbsEntry: Int,
    val BankCode: String,
    val BankName: String,
    val CountryCod: String
):MappingInteface<Banco>(){
    constructor(): this(
        AbsEntry = 0,
        BankCode = "",
        BankName = "",
        CountryCod = ""
    )
    override fun map(data: List<Banco>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Banco>): List<Any> {
        return data.map { it.AbsEntry }
    }

}

