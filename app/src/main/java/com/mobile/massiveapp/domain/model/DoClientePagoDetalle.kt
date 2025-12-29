package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ClientePagosDetalleEntity
import com.mobile.massiveapp.data.model.ClientePagosDetalle

data class DoClientePagoDetalle(
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
    val SumApplied: Double,
    val ObjType: Int,
    val AccControl: String
){
    constructor(): this(
        AccAction = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccDocEntry = "",
        AccMigrated = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        AppliedFC = 0.0,
        DocEntry = -1,
        DocLine = -1,
        DocNum = -1,
        DocTransId = -1,
        InstId = -1,
        InvType = -1,
        SumApplied = 0.0,
        AccControl = "",
        ObjType = -1

    )
}

fun ClientePagosDetalleEntity.toDomain() = DoClientePagoDetalle(
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
    SumApplied = SumApplied,
    AccControl = AccControl,
    ObjType = ObjType
)

fun ClientePagosDetalle.toDomain() = DoClientePagoDetalle(
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
    SumApplied = SumApplied,
    AccControl = AccControl,
    ObjType = ObjType
)

data class DoPagoDetalle(
    val NumeroFactura: String,
    val DocEntry: Int,
    var DocLine: Int,
    val AccCreateDate: String,
    val SumApplied: Double
)