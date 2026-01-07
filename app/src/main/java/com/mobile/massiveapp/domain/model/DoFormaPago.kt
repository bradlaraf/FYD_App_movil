package com.mobile.massiveapp.domain.model

data class DoFormaPago (
    val Code: String,
    val Name: String,
    val U_MSV_MA_CUENTA: String,
) {
    constructor():this(
        Code = "",
        Name = "",
        U_MSV_MA_CUENTA = "",
    )
}

data class DoFormaPagoItemView(
    val Id: Int,
    val Title: String,
    val Value: String,
    val Arrow: String
)