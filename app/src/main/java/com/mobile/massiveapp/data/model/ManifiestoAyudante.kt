package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ManifiestoAyudante(
    val DocEntry: Int,
    val LineId: Int,
    val U_MSV_MA_CODIGO: Int,
    val U_MSV_MA_NOMBRE: String,
): MappingInteface<ManifiestoAyudante>(){
    constructor(): this(
        DocEntry = -1,
        LineId = -1,
        U_MSV_MA_CODIGO = -1,
        U_MSV_MA_NOMBRE = "",
    )
    override fun map(data: List<ManifiestoAyudante>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ManifiestoAyudante>): List<Any> {
        return data.map { "${it.DocEntry}_${it.LineId}" }
    }

}
