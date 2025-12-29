package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.SociedadEntity
import com.mobile.massiveapp.data.model.Sociedad

data class DoSociedad(
    val AliasName: String,
    val CompnyName: String,
    val E_Mail: String,
    val TaxIdNum: String,
    val WebSite: String,
    val PriceDec: Int
)

fun SociedadEntity.toDomain() = DoSociedad(
    AliasName = AliasName,
    CompnyName = CompnyName,
    E_Mail = E_Mail,
    TaxIdNum = TaxIdNum,
    WebSite = WebSite,
    PriceDec = PriceDec
)

fun Sociedad.toDomain() = DoSociedad(
    AliasName = AliasName,
    CompnyName = CompnyName,
    E_Mail = E_Mail,
    TaxIdNum = TaxIdNum,
    WebSite = WebSite,
    PriceDec = PriceDec
)
