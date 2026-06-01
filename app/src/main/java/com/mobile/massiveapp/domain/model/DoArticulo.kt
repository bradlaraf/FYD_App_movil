package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloEntity
import com.mobile.massiveapp.data.model.Articulo

data class DoArticulo (
    val ItemCode: String,
    val AccLocked: String,
    val FirmCode: Int,
    val InvntItem: String,
    val InvntryUom: String,
    val ItemName: String,
    val ItmsGrpCod: Int,
    val IuoMEntry: Int,
    val SalUnitMsr: String,
    val NumInSale: Int,
    val SalPackMsr: String,
    val SalPackUn: Int,
    val SuoMEntry: Int,
    val UgpEntry: Int,
    var OnHand: Double,
    var GrupoArticulo: String
    ) {
    constructor(): this(
        AccLocked = "",
        FirmCode = 0,
        InvntItem = "",
        InvntryUom = "",
        ItemCode = "",
        ItemName = "",
        ItmsGrpCod = 0,
        IuoMEntry = 0,
        SalUnitMsr = "",
        NumInSale = 0,
        SalPackMsr = "",
        SalPackUn = 0,
        SuoMEntry = 0,
        UgpEntry = 0,
        OnHand = 0.0,
        GrupoArticulo = ""
    )
}

fun Articulo.toDomain()= DoArticulo(
    FirmCode=FirmCode,
    InvntItem=InvntItem,
    InvntryUom=InvntryUom,
    ItemCode=ItemCode,
    ItemName=ItemName,
    ItmsGrpCod=ItmsGrpCod,
    IuoMEntry=IuoMEntry,
    SalUnitMsr=SalUnitMsr,
    NumInSale = NumInSale,
    SalPackMsr = SalPackMsr,
    SalPackUn = SalPackUn,
    SuoMEntry=SuoMEntry,
    UgpEntry=UgpEntry,
    OnHand = 0.0,
    AccLocked = AccLocked,
    GrupoArticulo = ""
)

fun ArticuloEntity.toDomain()= DoArticulo(
    FirmCode=FirmCode,
    InvntItem=InvntItem,
    InvntryUom=InvntryUom,
    ItemCode=ItemCode,
    ItemName=ItemName,
    ItmsGrpCod=ItmsGrpCod,
    IuoMEntry=IuoMEntry,
    SalUnitMsr=SalUnitMsr,
    NumInSale = NumInSale,
    SalPackMsr = SalPackMsr,
    SalPackUn = SalPackUn,
    SuoMEntry=SuoMEntry,
    UgpEntry=UgpEntry,
    OnHand = 0.0,
    AccLocked = AccLocked,
    GrupoArticulo = ""
)

data class DoArticuloInventario(
    val ItemCode: String,
    val ItemName: String,
    var OnHand: Double,
    var GrupoArticulo: String
)

data class DoArticuloInv(
    val ItemCode: String,
    val ItemName: String,
    val WhsName1: String,
    val WhsName2: String,
    var OnHand1: Double,
    var OnHand2: Double,
    var GrupoArticulo: String
)

data class TipoUnidadView(
    val Name: String,
    val Code: String
)

fun DoArticuloInv.toInv() = DoArticuloInventario(
    ItemCode = ItemCode,
    ItemName = ItemName,
    OnHand = OnHand1,
    GrupoArticulo = GrupoArticulo
)
