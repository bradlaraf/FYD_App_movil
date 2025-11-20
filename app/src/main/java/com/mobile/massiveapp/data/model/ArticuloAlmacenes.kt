package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloAlmacenes(
    val Block: String,
    val City: String,
    val Country: String,
    val County: String,
    val Principal: String,
    val State: String,
    val Street: String,
    val TypeW: String,
    val Ubigeo: String,
    val WhsCode: String,
    val WhsName: String
):MappingInteface<ArticuloAlmacenes>(){
    constructor() : this(
        Block = "",
        City = "",
        Country = "",
        County = "",
        Principal = "",
        State = "",
        Street = "",
        TypeW = "",
        Ubigeo = "",
        WhsCode = "",
        WhsName = ""
    )
    override fun map(data: List<ArticuloAlmacenes>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloAlmacenes>): List<Any> {
        return data.map { it.WhsCode }
    }

}