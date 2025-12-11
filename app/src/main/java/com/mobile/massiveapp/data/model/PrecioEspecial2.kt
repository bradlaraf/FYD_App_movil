package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.PrecioEspecial2Entity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class PrecioEspecial2(
    val Code: String,
    val ItemCode: String,
    val CardCode: String,
    val SPP1LNum: Int,
    val SPP2LNum: Int,
    val Amount: Double,
    val Price: Double,
    val Currency: String,
    val Discount: Double,
    val UomEntry: Int
):MappingInteface<PrecioEspecial2>(){
    constructor(): this(
        Code = "",
        ItemCode = "",
        CardCode = "",
        SPP1LNum = 0,
        SPP2LNum = 0,
        Amount = 0.0,
        Price = 0.0,
        Currency = "",
        Discount = 0.0,
        UomEntry = 0,
    )
    override fun map(data: List<PrecioEspecial2>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<PrecioEspecial2>): List<Any> {
        return data.map { it.Code }
    }

}

fun PrecioEspecial2Entity.toModel() = PrecioEspecial2(
    Code = Code,
    ItemCode = ItemCode,
    CardCode = CardCode,
    SPP1LNum = SPP1LNum,
    SPP2LNum = SPP2LNum,
    Amount = Amount,
    Price = Price,
    Currency = Currency,
    Discount = Discount,
    UomEntry = UomEntry,
)
