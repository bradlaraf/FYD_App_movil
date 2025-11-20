package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle

data class ClientePagosDetalle(
    val AccAction: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccDocEntry: String,
    val AccMigrated: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val AppliedFC: Double,
    val DocEntry: Int,
    var DocLine: Int,
    val DocNum: Int,
    val DocTransId: Int,
    val InstId: Int,
    val InvType: Int,
    val SumApplied: Double
):MappingInteface<ClientePagosDetalle>(){
    constructor():this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0.0,
        0,
        0,
        0,
        0,
        0,
        0,
        0.0,
    )

    override fun map(data: List<ClientePagosDetalle>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ClientePagosDetalle>): List<Any> {
        return data.map { "${it.AccDocEntry}${it.DocLine}${it.DocEntry}" }
    }
}

fun DoClientePagoDetalle.toModel() = ClientePagosDetalle(
    AccAction = AccAction,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccDocEntry = AccDocEntry,
    AccMigrated = AccMigrated,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
    AppliedFC = AppliedFC,
    DocEntry = DocEntry,
    DocLine = DocLine,
    DocNum = DocNum,
    DocTransId = DocTransId,
    InstId = InstId,
    InvType = InvType,
    SumApplied = SumApplied
)