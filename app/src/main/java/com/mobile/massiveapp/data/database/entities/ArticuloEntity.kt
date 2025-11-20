package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Articulo
import com.mobile.massiveapp.domain.model.DoArticulo

@Entity(tableName = "Articulo")
data class ArticuloEntity (
    @PrimaryKey
    @ColumnInfo(name = "ItemCode") val ItemCode: String,
    @ColumnInfo(name = "AccLocked") val AccLocked: String,
    @ColumnInfo(name = "FirmCode") val FirmCode: Int,
    @ColumnInfo(name = "InvntItem") val InvntItem: String,
    @ColumnInfo(name = "InvntryUom") val InvntryUom: String,
    @ColumnInfo(name = "ItemName") val ItemName: String,
    @ColumnInfo(name = "ItmsGrpCod") val ItmsGrpCod: Int,
    @ColumnInfo(name = "IuoMEntry") val IuoMEntry: Int,
    @ColumnInfo(name = "SalUnitMsr") val SalUnitMsr: String,
    @ColumnInfo(name = "SuoMEntry") val SuoMEntry: Int,
    @ColumnInfo(name = "UgpEntry") val UgpEntry: Int
    )

fun DoArticulo.toDatabase()= ArticuloEntity(
    FirmCode=FirmCode,
    InvntItem=InvntItem,
    InvntryUom=InvntryUom,
    ItemCode=ItemCode,
    ItemName=ItemName,
    ItmsGrpCod=ItmsGrpCod,
    IuoMEntry=IuoMEntry,
    SalUnitMsr=SalUnitMsr,
    SuoMEntry=SuoMEntry,
    UgpEntry=UgpEntry,
    AccLocked = AccLocked
)

fun Articulo.toDatabase() = ArticuloEntity(
    FirmCode=FirmCode,
    InvntItem=InvntItem,
    InvntryUom=InvntryUom,
    ItemCode=ItemCode,
    ItemName=ItemName,
    ItmsGrpCod=ItmsGrpCod,
    IuoMEntry=IuoMEntry,
    SalUnitMsr=SalUnitMsr,
    SuoMEntry=SuoMEntry,
    UgpEntry=UgpEntry,
    AccLocked = AccLocked
)