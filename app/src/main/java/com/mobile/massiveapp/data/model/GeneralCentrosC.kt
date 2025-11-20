package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralCentrosC(
    val DimCode: Int,
    val PrcCode: String,
    val PrcName: String
):MappingInteface<GeneralCentrosC>(){
    constructor(): this(
        DimCode = 0,
        PrcCode = "",
        PrcName = ""
    )
    override fun map(data: List<GeneralCentrosC>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralCentrosC>): List<Any> {
        return data.map { it.DimCode }
    }

}