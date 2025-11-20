package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.ClienteFacturaDetalle

@Entity(tableName = "FacturaDetalle", primaryKeys = ["DocEntry", "LineNum"])
data class ClienteFacturaDetalleEntity (
    @ColumnInfo(name ="DiscPrcnt") val DiscPrcnt: Double,
    @ColumnInfo(name ="DocEntry") val DocEntry: Int,
    @ColumnInfo(name ="Dscription") val Dscription: String,
    @ColumnInfo(name ="ItemCode") val ItemCode: String,
    @ColumnInfo(name ="LineNum") val LineNum: Int,
    @ColumnInfo(name ="LineTotal") val LineTotal: Double,
    @ColumnInfo(name ="Price") val Price: Double,
    @ColumnInfo(name ="PriceBefDi") val PriceBefDi: Double,
    @ColumnInfo(name ="Quantity") val Quantity: Double,
    @ColumnInfo(name ="UnitMsr") val UnitMsr: String,
    @ColumnInfo(name ="WhsCode") val WhsCode: String,
    @ColumnInfo(name ="PriceList") val PriceList: Int
)

fun ClienteFacturaDetalle.toDatabase() = ClienteFacturaDetalleEntity(
    DocEntry = DocEntry,
    LineNum = LineNum,
    ItemCode = ItemCode,
    Dscription = Dscription,
    UnitMsr = UnitMsr,
    WhsCode = WhsCode,
    Quantity = Quantity,
    PriceBefDi = PriceBefDi,
    DiscPrcnt = DiscPrcnt,
    Price = Price,
    LineTotal = LineTotal,
    PriceList = PriceList
)