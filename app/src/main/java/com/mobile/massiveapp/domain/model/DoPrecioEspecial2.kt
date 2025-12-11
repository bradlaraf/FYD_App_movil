package com.mobile.massiveapp.domain.model

data class DoPrecioEspecial2(
    val Code: String,
    val ItemCode: String,
    val CardCode: String,
    val SPP1LNum: Int,
    val SPP2LNum: Int,
    val Amount: Double,
    val Price: Double,
    val Currency: String,
    val Discount: Double,
    val UomEntry: Int
)
