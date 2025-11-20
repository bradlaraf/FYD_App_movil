package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.ClienteFacturaDetalleEntity
import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class ClienteFacturaDetalle(
    val DiscPrcnt: Double,
    val DocEntry: Int,
    val Dscription: String,
    val ItemCode: String,
    val LineNum: Int,
    val LineTotal: Double,
    val Price: Double,
    val PriceBefDi: Double,
    val PriceList: Int,
    val Quantity: Double,
    val UnitMsr: String,
    val WhsCode: String
):MappingInteface<ClienteFacturaDetalle>(){
    constructor() : this(
        DiscPrcnt = 0.0,
        DocEntry = 0,
        Dscription = "",
        ItemCode = "",
        LineNum = 0,
        LineTotal = 0.0,
        Price = 0.0,
        PriceBefDi = 0.0,
        Quantity = 0.0,
        UnitMsr = "",
        WhsCode = "",
        PriceList = 0
    )



    override fun map(data: List<ClienteFacturaDetalle>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ClienteFacturaDetalle>): List<Any> {
        return data.map { "${it.DocEntry}${it.LineNum}" }
    }
}

fun ClienteFacturaDetalleEntity.toModel() = ClienteFacturaDetalle(
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