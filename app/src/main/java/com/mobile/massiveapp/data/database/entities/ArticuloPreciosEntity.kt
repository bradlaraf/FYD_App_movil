package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.ArticuloPrecio
import com.mobile.massiveapp.domain.model.DoArticuloPrecios

@Entity(tableName = "ArticuloPrecio", primaryKeys = ["ItemCode", "PriceList"])
data class ArticuloPreciosEntity(
    @ColumnInfo(name = "ItemCode") val ItemCode: String,
    @ColumnInfo(name = "PriceList") val PriceList: Int,
    @ColumnInfo(name = "Price") val Price: Double,
    @ColumnInfo(name = "AddPrice") val AddPrice: Double,
    @ColumnInfo(name = "AddPrice2") val AddPrice2: Double
)

fun DoArticuloPrecios.toDatabase() = ArticuloPreciosEntity(
    ItemCode = ItemCode,
    PriceList = PriceList,
    Price = Price,
    AddPrice = AddPrice,
    AddPrice2 = AddPrice2)

fun ArticuloPrecio.toDatabase() = ArticuloPreciosEntity(
    ItemCode = ItemCode,
    PriceList = PriceList,
    Price = Price,
    AddPrice = AddPrice,
    AddPrice2 = AddPrice2)