package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.PrecioEspecial2
import com.mobile.massiveapp.domain.model.DoPrecioEspecial2

@Entity(tableName = "PrecioEspecial2")
data class PrecioEspecial2Entity(
    @PrimaryKey
    @ColumnInfo("Code") val Code: String,
    @ColumnInfo("ItemCode") val ItemCode: String,
    @ColumnInfo("CardCode") val CardCode: String,
    @ColumnInfo("SPP1LNum") val SPP1LNum: Int,
    @ColumnInfo("SPP2LNum") val SPP2LNum: Int,
    @ColumnInfo("Amount") val Amount: Double,
    @ColumnInfo("Price") val Price: Double,
    @ColumnInfo("Currency") val Currency: String,
    @ColumnInfo("Discount") val Discount: Double,
    @ColumnInfo("UomEntry") val UomEntry: Int
)
fun PrecioEspecial2.toDatabase() = PrecioEspecial2Entity(
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

fun DoPrecioEspecial2.toDatabase() = PrecioEspecial2Entity(
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