package com.mobile.massiveapp.domain.model

data class DoCuentasC (
    val AcctCode: String,
    val AcctName: String
){
    constructor(): this(
        AcctCode = "",
        AcctName = "",
    )
}