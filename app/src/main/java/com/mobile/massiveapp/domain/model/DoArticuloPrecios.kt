package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloPreciosEntity
import com.mobile.massiveapp.data.model.ArticuloPrecio

data class DoArticuloPrecios(
    val AddPrice: Double,
    val AddPrice2: Double,
    val ItemCode: String,
    var Price: Double,
    val PriceList: Int,
    val ListName: String
) {
    constructor() : this(0.0, 0.0, "", 0.0, 0, "")

    //Seter price
    fun setPrice(price: Double) = this.copy(Price = price)
}

fun ArticuloPrecio.toDomain() = DoArticuloPrecios(
    AddPrice = AddPrice,
    AddPrice2 = AddPrice2,
    ItemCode = ItemCode,
    Price = Price,
    PriceList = PriceList,
    ListName = ""
)

fun ArticuloPreciosEntity.toDomain() = DoArticuloPrecios(
    AddPrice = AddPrice,
    AddPrice2 = AddPrice2,
    ItemCode = ItemCode,
    Price = Price,
    PriceList = PriceList,
    ListName = ""
)

fun ArticuloPreciosEntity.toDomainWithListName(listName: String) = DoArticuloPrecios(
    AddPrice = AddPrice,
    AddPrice2 = AddPrice2,
    ItemCode = ItemCode,
    Price = Price,
    PriceList = PriceList,
    ListName = listName
)
