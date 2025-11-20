package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloUnidades(
    val UomCode: String,
    val UomEntry: Int,
    val UomName: String
):MappingInteface<ArticuloUnidades>() {
    constructor(): this(
        UomCode = "",
        UomEntry = -1,
        UomName = ""
    )
    override fun map(data: List<ArticuloUnidades>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloUnidades>): List<Any> {
        return data.map { it.UomEntry }
    }

}