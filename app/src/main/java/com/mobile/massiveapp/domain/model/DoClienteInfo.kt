package com.mobile.massiveapp.domain.model

data class DoClienteInfo(
    val CardName: String,
    val CardCode: String,
    var CardType: String,
    var U_MSV_LO_TIPOPER: String,
    var U_MSV_LO_TIPODOC: String,
    val LicTradNum: String,
    val U_MSV_LO_APELPAT: String,
    val U_MSV_LO_APELMAT: String,
    val U_MSV_LO_PRIMNOM: String,
    val U_MSV_LO_SEGUNOM: String,
    val GroupName: String,
    val Currency: String,
    val E_Mail: String,
    val Phone1: String,
    val Phone2: String,
    val Cellular: String,
    val PymntGroup: String,
    val ListName: String,
    val Indicador: String,
    var Zona: String
){
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
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}
