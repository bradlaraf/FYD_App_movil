package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.PrecioEspecial
import com.mobile.massiveapp.domain.model.DoPrecioEspecial

@Entity("PrecioEspecial")
data class PrecioEspecialEntity(
    @PrimaryKey
    @ColumnInfo("Code") val Code: String,
    @ColumnInfo("ItemCode") val ItemCode: String,
    @ColumnInfo("CardCode") val CardCode: String,
    @ColumnInfo("Price") val Price: Double,
    @ColumnInfo("Currency") val Currency: String,
    @ColumnInfo("Discount") val Discount: Double,
    @ColumnInfo("ListNum") val ListNum: Int,
    @ColumnInfo("ValidFrom") val ValidFrom: String,
    @ColumnInfo("ValidTo") val ValidTo: String
)

fun PrecioEspecial.toDatabase() = PrecioEspecialEntity(
    Code = Code,
    ItemCode = ItemCode,
    CardCode = CardCode,
    Price = Price,
    Currency = Currency,
    Discount = Discount,
    ListNum = ListNum,
    ValidFrom = ValidFrom,
    ValidTo = ValidTo
)

fun DoPrecioEspecial.toDatabase() = PrecioEspecialEntity(
    Code = Code,
    ItemCode = ItemCode,
    CardCode = CardCode,
    Price = Price,
    Currency = Currency,
    Discount = Discount,
    ListNum = ListNum,
    ValidFrom = ValidFrom,
    ValidTo = ValidTo
)
