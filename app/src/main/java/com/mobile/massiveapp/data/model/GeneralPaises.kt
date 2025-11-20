package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralPaises(
    val Code: String,
    val Name: String
):MappingInteface<GeneralPaises>(){
    constructor(): this(
        Code = "",
        Name = ""
    )
    override fun map(data: List<GeneralPaises>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralPaises>): List<Any> {
        return data.map { it.Code }
    }

}