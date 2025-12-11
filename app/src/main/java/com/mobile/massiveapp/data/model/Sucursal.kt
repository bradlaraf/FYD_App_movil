package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Sucursal (
    val Code: String,
    val Name: String
):MappingInteface<Sucursal>() {
    constructor():this(
        Code = "",
        Name = ""
    )
    override fun map(data: List<Sucursal>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Sucursal>): List<Any> {
        return data.map { it.Code }
    }
}