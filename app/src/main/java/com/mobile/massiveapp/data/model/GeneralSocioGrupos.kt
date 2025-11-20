package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class GeneralSocioGrupos(
    val GroupCode: String,
    val GroupName: String,
    val GroupType: String
):MappingInteface<GeneralSocioGrupos>(){
    constructor(): this(
        GroupCode = "",
        GroupName = "",
        GroupType = ""
    )
    override fun map(data: List<GeneralSocioGrupos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<GeneralSocioGrupos>): List<Any> {
        return data.map { it.GroupCode }
    }

}