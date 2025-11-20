package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloGruposUMDetalle(
    val AltQty: Double,
    val BaseQty: Double,
    val LineNum: Int,
    val UgpEntry: Int,
    val UomEntry: Int
): MappingInteface<ArticuloGruposUMDetalle>() {
    constructor(): this(
        AltQty = 0.0,
        BaseQty = 0.0,
        LineNum = 0,
        UgpEntry = 0,
        UomEntry = 0
    )
    override fun map(data: List<ArticuloGruposUMDetalle>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloGruposUMDetalle>): List<Any> {
        return data.map { "${it.UgpEntry}${it.UomEntry}" }
    }
}

