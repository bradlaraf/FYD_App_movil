package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralCondiciones(
    val ExtraDays: Int,
    val ExtraMonth: Int,
    val GroupNum: Int,
    val PymntGroup: String
):MappingInteface<GeneralCondiciones>(){
    constructor() : this(
        -11,
        -11,
        -11,
        "")

    override fun map(data: List<GeneralCondiciones>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralCondiciones>): List<Any> {
        return data.map { it.GroupNum }
    }
}