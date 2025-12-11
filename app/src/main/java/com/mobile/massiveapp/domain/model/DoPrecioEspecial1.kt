package com.mobile.massiveapp.domain.model

data class DoPrecioEspecial1(
    val Code: String,
    val ItemCode: String,
    val CardCode: String,
    val LINENUM: Int,
    val Price: Double,
    val Currency: String,
    val Discount: Double,
    val ListNum: Int,
    val FromDate: String,
    val ToDate: String
)
