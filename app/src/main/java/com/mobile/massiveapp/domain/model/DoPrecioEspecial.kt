package com.mobile.massiveapp.domain.model

data class DoPrecioEspecial(
    val Code: String,
    val ItemCode: String,
    val CardCode: String,
    val Price: Double,
    val Currency: String,
    val Discount: Double,
    val ListNum: Int,
    val ValidFrom: String,
    val ValidTo: String
)
