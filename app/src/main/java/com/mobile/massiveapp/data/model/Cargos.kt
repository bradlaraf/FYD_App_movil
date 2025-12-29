package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.CargosEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Cargos (
    val posID: Int,
    val name: String,
    val descriptio: String,
): MappingInteface<Cargos>(){
    constructor():this(
        posID = -1,
        name = "",
        descriptio = "",
    )
    override fun map(data: List<Cargos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Cargos>): List<Any> {
        return data.map { it.posID }
    }

}
fun CargosEntity.toModel() = Cargos(
    posID = posID,
    name = name,
    descriptio = descriptio,
)
