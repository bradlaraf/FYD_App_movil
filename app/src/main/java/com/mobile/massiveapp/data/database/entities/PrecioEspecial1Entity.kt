package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.PrecioEspecial1
import com.mobile.massiveapp.domain.model.DoPrecioEspecial1

@Entity(tableName = "PrecioEspecial1")
data class PrecioEspecial1Entity(
    @PrimaryKey
    @ColumnInfo("Code") val Code: String,
    @ColumnInfo("ItemCode") val ItemCode: String,
    @ColumnInfo("CardCode") val CardCode: String,
    @ColumnInfo("LINENUM") val LINENUM: Int,
    @ColumnInfo("Price") val Price: Double,
    @ColumnInfo("Currency") val Currency: String,
    @ColumnInfo("Discount") val Discount: Double,
    @ColumnInfo("ListNum") val ListNum: Int,
    @ColumnInfo("FromDate") val FromDate: String,
    @ColumnInfo("ToDate") val ToDate: String
)

fun PrecioEspecial1.toDatabase() = PrecioEspecial1Entity(
    Code = Code,
    ItemCode = ItemCode,
    CardCode = CardCode,
    LINENUM = LINENUM,
    Price = Price,
    Currency = Currency,
    Discount = Discount,
    ListNum = ListNum,
    FromDate = FromDate,
    ToDate = ToDate
)

fun DoPrecioEspecial1.toDatabase() = PrecioEspecial1Entity(
    Code = Code,
    ItemCode = ItemCode,
    CardCode = CardCode,
    LINENUM = LINENUM,
    Price = Price,
    Currency = Currency,
    Discount = Discount,
    ListNum = ListNum,
    FromDate = FromDate,
    ToDate = ToDate
)