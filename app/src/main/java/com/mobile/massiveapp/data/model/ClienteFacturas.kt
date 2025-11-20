package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface
import com.google.gson.annotations.SerializedName

data class ClienteFacturas(
    val Address: String,
    val Address2: String,
    val CardCode: String,
    val CardName: String,
    val Comments: String,
    val DocCur: String,
    val DocDate: String,
    val DocDueDate: String,
    var DocEntry: Int,
    val DocNum: Int,
    val DocRate: Double,
    val DocStatus: String,
    val DocTotal: Double,
    val FolioNum: Int,
    val FolioPref: String,
    val GroupNum: Int,
    val Indicator: String,
    val InstlmntID: Int,
    val LicTradNum: String,
    val NumAtCard: String,
    val PaidToDate: Double,
    val PayToCode: String,
    val ShipToCode: String,
    val SlpCode: Int,
    val TaxDate: String,
    @SerializedName("Lineas")
    val facturasDetalle: List<ClienteFacturaDetalle>
):MappingInteface<ClienteFacturas>() {
    constructor():this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        0,
        0.0,
        "",
        0.0,
        0,
        "",
        0,
        "",
        0,
        "",
        "",
        0.0,
        "",
        "",
        0,
         "",
        emptyList()
    )

    override fun map(data: List<ClienteFacturas>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ClienteFacturas>): List<Any> {
        return data.map {it.DocEntry}
    }
}
