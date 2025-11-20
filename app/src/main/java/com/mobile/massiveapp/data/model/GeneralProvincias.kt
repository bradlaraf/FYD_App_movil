package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralProvincias(
    val Code: String,
    val Name: String
): MappingInteface<GeneralProvincias>(){
    constructor(): this (
        Code = "",
        Name = ""
            )
    override fun map(data: List<GeneralProvincias>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralProvincias>): List<Any> {
        return data.map { it.Code }
    }

}