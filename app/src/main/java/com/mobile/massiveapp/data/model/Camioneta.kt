package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class Camioneta(
    val Code: String,
    val Name: String,
    val U_MSV_MA_PLACA: String,
    val U_MSV_MA_MARCA: String,
    val U_MSV_MA_MODELO: String,
    val U_MSV_MA_TOLVA: String,
    val U_MSV_MA_ANIOFAB: Int
): MappingInteface<Camioneta> () {
    constructor() : this(
        Code = "",
        Name = "",
        U_MSV_MA_PLACA = "",
        U_MSV_MA_MARCA = "",
        U_MSV_MA_MODELO = "",
        U_MSV_MA_TOLVA = "",
        U_MSV_MA_ANIOFAB = 0
    )

    override fun map(data: List<Camioneta>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<Camioneta>): List<Any> {
        return data.map { it.Code}
    }

}
