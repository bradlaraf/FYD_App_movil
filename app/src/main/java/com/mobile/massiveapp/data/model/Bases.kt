package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Bases(
    val Code: String,
    val CompnyName: String,
    val DataBase: String
): MappingInteface<Bases>() {

    constructor(): this(
        Code = "",
        CompnyName = "",
        DataBase = ""
    )
    override fun map(data: List<Bases>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Bases>): List<Any> {
        return data.map { it.Code }
    }

}