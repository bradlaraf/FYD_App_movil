package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class CuentasC(
    val AcctCode: String,
    val AcctName: String
): MappingInteface<CuentasC>() {

    constructor(): this(
        AcctCode = "",
        AcctName = ""
    )

    override fun map(data: List<CuentasC>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<CuentasC>): List<Any> {
        return emptyList()
    }
}