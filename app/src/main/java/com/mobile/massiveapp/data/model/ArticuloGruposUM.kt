package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloGruposUM(
    val UgpCode: String,
    val UgpEntry: Int,
    val UgpName: String,
    val Lineas: List<ArticuloGruposUMDetalle>
):MappingInteface<ArticuloGruposUM>(){
    constructor(): this(
        UgpCode = "",
        UgpEntry = 0,
        UgpName = "",
        Lineas = emptyList()
    )
    override fun map(data: List<ArticuloGruposUM>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloGruposUM>): List<Any> {
        return data.map { it.UgpEntry }
    }

}