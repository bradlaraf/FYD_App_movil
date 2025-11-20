package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralProyectos(
    val PrjCode: String,
    val PrjName: String
): MappingInteface<GeneralProyectos>(){
    constructor():this(
        PrjCode = "",
        PrjName = ""
    )
    override fun map(data: List<GeneralProyectos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralProyectos>): List<Any> {
        return data.map { it.PrjCode }
    }

}