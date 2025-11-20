package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.mobile.massiveapp.data.model.ClientePagosDetalle
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle

@Entity(tableName = "ClientePagosDetalle", primaryKeys = ["AccDocEntry", "DocLine", "DocEntry"])
data class ClientePagosDetalleEntity (
    @ColumnInfo(name = "DocEntry") val DocEntry: Int,
    @ColumnInfo(name = "DocLine") val DocLine: Int,
    @ColumnInfo(name = "DocNum") val DocNum: Int,
    @ColumnInfo(name = "DocTransId") val DocTransId: Int,
    @ColumnInfo(name = "InstId") val InstId: Int,
    @ColumnInfo(name = "InvType") val InvType: Int,
    @ColumnInfo(name = "SumApplied") val SumApplied: Double,
    @ColumnInfo(name = "AccAction") val AccAction: String,
    @ColumnInfo(name = "AccCreateDate") val AccCreateDate: String,
    @ColumnInfo(name = "AccCreateHour") val AccCreateHour: String,
    @ColumnInfo(name = "AccCreateUser") val AccCreateUser: String,
    @ColumnInfo(name = "AccDocEntry") val AccDocEntry: String,
    @ColumnInfo(name = "AccMigrated") val AccMigrated: String,
    @ColumnInfo(name = "AccUpdateDate") val AccUpdateDate: String,
    @ColumnInfo(name = "AccUpdateHour") val AccUpdateHour: String,
    @ColumnInfo(name = "AccUpdateUser") val AccUpdateUser: String,
    @ColumnInfo(name = "AppliedFC") val AppliedFC: Double
    )

fun ClientePagosDetalle.toDatabase() = ClientePagosDetalleEntity(
    DocEntry = DocEntry,
    DocLine = DocLine,
    DocNum = DocNum,
    DocTransId = DocTransId,
    InstId = InstId,
    InvType = InvType,
    SumApplied = SumApplied,
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
)

fun DoClientePagoDetalle.toDatabase()= ClientePagosDetalleEntity(
    DocEntry = DocEntry,
    DocLine = DocLine,
    DocNum = DocNum,
    DocTransId = DocTransId,
    InstId = InstId,
    InvType = InvType,
    SumApplied = SumApplied,
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
)

fun ClientePagosDetalleEntity.toModel() = ClientePagosDetalle(
    DocEntry = DocEntry,
    DocLine = DocLine,
    DocNum = DocNum,
    DocTransId = DocTransId,
    InstId = InstId,
    InvType = InvType,
    SumApplied = SumApplied,
    AccAction = AccAction,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccDocEntry = AccDocEntry,
    AccMigrated = AccMigrated,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
    AppliedFC = AppliedFC
)