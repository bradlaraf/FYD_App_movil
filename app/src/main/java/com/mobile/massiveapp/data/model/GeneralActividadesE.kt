package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralActividadesE(
    val Code: String,
    val Name: String
):MappingInteface<GeneralActividadesE>() {
    constructor(): this(
        Code = "",
        Name = ""
    )
    override fun map(data: List<GeneralActividadesE>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralActividadesE>): List<Any> {
        return data.map { it.Code }
    }
}