package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ArticuloPrecio(
    val AddPrice: Double,
    val AddPrice2: Double,
    val ItemCode: String,
    val Price: Double,
    val PriceList: Int
): MappingInteface<ArticuloPrecio>() {
    constructor() : this(
        ItemCode = "",
        PriceList = 0,
        Price = 0.0,
        AddPrice = 0.0,
        AddPrice2 = 0.0
    )

    override fun map(data: List<ArticuloPrecio>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ArticuloPrecio>): List<Any> {
        return data.map { "${it.ItemCode}_${it.PriceList}" }
    }
}
