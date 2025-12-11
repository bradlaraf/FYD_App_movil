package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.PrecioEspecialEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class PrecioEspecial(
    val Code: String,
    val ItemCode: String,
    val CardCode: String,
    val Price: Double,
    val Currency: String,
    val Discount: Double,
    val ListNum: Int,
    val ValidFrom: String,
    val ValidTo: String,
    val Linea1: List<PrecioEspecial1>,
    val Linea2: List<PrecioEspecial2>
): MappingInteface<PrecioEspecial>(){

    constructor():this(
        Code = "",
        ItemCode = "",
        CardCode = "",
        Price = 0.0,
        Currency = "",
        Discount = 0.0,
        ListNum = 0,
        ValidFrom = "",
        ValidTo = "",
        Linea1 = emptyList(),
        Linea2 = emptyList()
    )

    override fun map(data: List<PrecioEspecial>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<PrecioEspecial>): List<Any> {
        return data.map { it.Code }
    }

}

fun PrecioEspecialEntity.toModel(linea1:List<PrecioEspecial1>, linea2:List<PrecioEspecial2>) = PrecioEspecial(
    Code = Code,
    ItemCode = ItemCode,
    CardCode = CardCode,
    Price = Price,
    Currency = Currency,
    Discount = Discount,
    ListNum = ListNum,
    ValidFrom = ValidFrom,
    ValidTo = ValidTo,
    Linea1 = linea1,
    Linea2 = linea2
)
