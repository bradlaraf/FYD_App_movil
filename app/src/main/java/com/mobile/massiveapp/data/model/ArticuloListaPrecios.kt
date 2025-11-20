package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloListaPrecios(
    val AddCurr1: String,
    val AddCurr2: String,
    val IsGrossPrc: String,
    val ListName: String,
    val ListNum: Int,
    val PrimCurr: String
):MappingInteface<ArticuloListaPrecios>(){
    constructor(): this(
        AddCurr1 = "",
        AddCurr2 = "",
        IsGrossPrc = "",
        ListName = "",
        ListNum = -1,
        PrimCurr = ""
    )
    override fun map(data: List<ArticuloListaPrecios>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloListaPrecios>): List<Any> {
        return data.map { it.ListNum }
    }

}