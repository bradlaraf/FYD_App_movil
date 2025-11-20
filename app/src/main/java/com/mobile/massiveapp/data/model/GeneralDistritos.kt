package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralDistritos(
    val Code: String,
    val Name: String
):MappingInteface<GeneralDistritos>() {
    constructor(): this(
        Code = "",
        Name = ""
    )
    override fun map(data: List<GeneralDistritos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralDistritos>): List<Any> {
        return data.map { it.Code }
    }

}