package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloLocalidades(
    val Code: Int,
    val Location: String
): MappingInteface<ArticuloLocalidades>(){
    constructor(): this(
        Code = -1,
        Location = ""
    )
    override fun map(data: List<ArticuloLocalidades>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloLocalidades>): List<Any> {
        return data.map { it.Code }
    }

}