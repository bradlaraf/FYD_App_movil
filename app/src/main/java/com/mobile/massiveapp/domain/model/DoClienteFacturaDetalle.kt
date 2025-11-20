package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ClienteFacturaDetalleEntity
import com.mobile.massiveapp.data.model.ClienteFacturaDetalle

data class DoClienteFacturaDetalle(
    val DiscPrcnt: Double,
    val DocEntry: Int,
    val Dscription: String,
    val ItemCode: String,
    val LineNum: Int,
    val LineTotal: Double,
    val Price: Double,
    val PriceBefDi: Double,
    val Quantity: Double,
    val UnitMsr: String,
    val WhsCode: String,
    val PriceList: Int
)


fun ClienteFacturaDetalleEntity.toDomain() = DoClienteFacturaDetalle(
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

fun ClienteFacturaDetalle.toDomain() = DoClienteFacturaDetalle(
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