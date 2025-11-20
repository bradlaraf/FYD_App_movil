package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloFabricantes(
    val FirmCode: Int,
    val FirmName: String
):MappingInteface<ArticuloFabricantes>(){
    constructor(): this(
        FirmCode = 0,
        FirmName = ""
    )
    override fun map(data: List<ArticuloFabricantes>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloFabricantes>): List<Any> {
        return data.map { it.FirmCode }
    }

}