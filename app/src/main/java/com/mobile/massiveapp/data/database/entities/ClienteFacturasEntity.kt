package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ClienteFacturas

@Entity(tableName = "Factura")
data class ClienteFacturasEntity(
    @PrimaryKey
    @ColumnInfo(name="DocEntry") val DocEntry: Int,
    @ColumnInfo(name="Address") val Address: String,
    @ColumnInfo(name="Address2") val Address2: String,
    @ColumnInfo(name="CardCode") val CardCode: String,
    @ColumnInfo(name="CardName") val CardName: String,
    @ColumnInfo(name="Comments") val Comments: String,
    @ColumnInfo(name="DocCur") val DocCur: String,
    @ColumnInfo(name="DocDate") val DocDate: String,
    @ColumnInfo(name="DocDueDate") val DocDueDate: String,
    @ColumnInfo(name="DocNum") val DocNum: Int,
    @ColumnInfo(name="DocRate") val DocRate: Double,
    @ColumnInfo(name="DocStatus") val DocStatus: String,
    @ColumnInfo(name="DocTotal") val DocTotal: Double,
    @ColumnInfo(name="FolioNum") val FolioNum: Int,
    @ColumnInfo(name="FolioPref") val FolioPref: String,
    @ColumnInfo(name="GroupNum") val GroupNum: Int,
    @ColumnInfo(name="Indicator") val Indicator: String,
    @ColumnInfo(name="InstlmntID") val InstlmntID: Int,
    @ColumnInfo(name="LicTradNum") val LicTradNum: String,
    @ColumnInfo(name="NumAtCard") val NumAtCard: String,
    @ColumnInfo(name="PaidToDate") val PaidToDate: Double,
    @ColumnInfo(name="Edit_Ptd") val Edit_Ptd: Double,
    @ColumnInfo(name="PayToCode") val PayToCode: String,
    @ColumnInfo(name="ShipToCode") val ShipToCode: String,
    @ColumnInfo(name="SlpCode") val SlpCode: Int,
    @ColumnInfo(name="TaxDate") val TaxDate: String,
    @ColumnInfo(name="Zona") val Zona: String
)

fun ClienteFacturas.toDatabase() = ClienteFacturasEntity(
    Address = Address,
    Address2 = Address2,
    CardCode = CardCode,
    CardName = CardName,
    Comments = Comments,
    DocCur = DocCur,
    DocDate = DocDate,
    DocDueDate = DocDueDate,
    DocEntry = DocEntry,
    DocNum = DocNum,
    DocRate = DocRate,
    DocStatus = DocStatus,
    DocTotal = DocTotal,
    FolioNum = FolioNum,
    FolioPref = FolioPref,
    GroupNum = GroupNum,
    Indicator = Indicator,
    InstlmntID = InstlmntID,
    LicTradNum = LicTradNum,
    NumAtCard = NumAtCard,
    PaidToDate = PaidToDate,
    PayToCode = PayToCode,
    ShipToCode = ShipToCode,
    SlpCode = SlpCode,
    TaxDate = TaxDate,
    Edit_Ptd = -11.0,
    Zona = "03"
)

fun ClienteFacturasEntity.toModel() = ClienteFacturas(
    Address = Address,
    Address2 = Address2,
    CardCode = CardCode,
    CardName = CardName,
    Comments = Comments,
    DocCur = DocCur,
    DocDate = DocDate,
    DocDueDate = DocDueDate,
    DocEntry = DocEntry,
    DocNum = DocNum,
    DocRate = DocRate,
    DocStatus = DocStatus,
    DocTotal = DocTotal,
    FolioNum = FolioNum,
    FolioPref = FolioPref,
    GroupNum = GroupNum,
    Indicator = Indicator,
    InstlmntID = InstlmntID,
    LicTradNum = LicTradNum,
    NumAtCard = NumAtCard,
    PaidToDate = PaidToDate,
    PayToCode = PayToCode,
    ShipToCode = ShipToCode,
    SlpCode = SlpCode,
    TaxDate = TaxDate,
    emptyList()

)
