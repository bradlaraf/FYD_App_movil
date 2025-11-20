package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloGrupos(
    val ItmsGrpCod: Int,
    val ItmsGrpNam: String
):MappingInteface<ArticuloGrupos>(){
    constructor(): this(
        ItmsGrpCod = -1,
        ItmsGrpNam = ""
    )
    override fun map(data: List<ArticuloGrupos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloGrupos>): List<Any> {
        return data.map { it.ItmsGrpCod }
    }

}