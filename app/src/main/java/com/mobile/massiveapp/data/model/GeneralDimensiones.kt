package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralDimensiones(
    val DimCode: Int,
    val DimDesc: String,
    val DimName: String
):MappingInteface<GeneralDimensiones>(){
    constructor(): this(
        DimCode = 0,
        DimDesc = "",
        DimName = ""
    )
    override fun map(data: List<GeneralDimensiones>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralDimensiones>): List<Any> {
        return data.map { it.DimCode }
    }
}