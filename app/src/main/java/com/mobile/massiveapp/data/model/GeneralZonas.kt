package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralZonas(
    val Code: String,
    val Name: String
):MappingInteface<GeneralZonas>() {
    constructor(): this(
        Code = "",
        Name = ""
    )
    override fun map(data: List<GeneralZonas>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralZonas>): List<Any> {
        return data.map { it.Code }
    }
}
