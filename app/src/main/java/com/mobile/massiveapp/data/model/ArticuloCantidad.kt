package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloCantidad(
    val ItemCode: String,
    val WhsCode: String,
    val OnHand: Double,
    val IsCommited: Double,
    val OnOrder: Double,
    val AvgPrice: Double,
):MappingInteface<ArticuloCantidad>(){
    constructor() : this(
        ItemCode = "",
        WhsCode = "",
        OnHand = 0.0,
        IsCommited = 0.0,
        OnOrder = 0.0,
        AvgPrice = 0.0
    )

    override fun map(data: List<ArticuloCantidad>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloCantidad>): List<Any> {
        return data.map { "${it.ItemCode}_${it.WhsCode}" }
    }
}
