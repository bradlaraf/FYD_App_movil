package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralDepartamentos(
    val Code: String,
    val Name: String
):MappingInteface<GeneralDepartamentos>() {
    constructor(): this(
        Code = "",
        Name = ""
    )

    override fun map(data: List<GeneralDepartamentos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralDepartamentos>): List<Any> {
        return data.map { it.Code }
    }
}