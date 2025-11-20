package com.mobile.massiveapp.domain.model

data class DoArticuloInfo(
    val ItemCode: String,
    val ItemName: String,
    val UnidadMedida: String,
    val FirmName: String,
    val ItmsGrpNam: String,
    val UgpName: String,
    val OnHand: Double,
    val OnOrder: Double,
    val IsCommited: Double,
    val WhsName: String,
){
    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        "",
        0.0,
        0.0,
        0.0,
        ""
    )
}
