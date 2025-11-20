package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralImpuestos(
    val Code: String,
    val Name: String,
    val Rate: Double
): MappingInteface<GeneralImpuestos>(){
    constructor(): this(
        Code = "",
        Name = "",
        Rate = 0.0
    )

    override fun map(data: List<GeneralImpuestos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralImpuestos>): List<Any> {
        return data.map { it.Code }
    }
}