package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Manifiesto (
    val Numero: String,
    val Conductor: String,
    val Vehiculo: String,
    val FechaSalida: String,
    val Estado: String
): MappingInteface<Manifiesto>(){
    constructor():this(
        "",
        "",
        "",
        "",
        ""
    )

    override fun map(data: List<Manifiesto>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Manifiesto>): List<Any> {
        return data.map {it.Numero}
    }
}