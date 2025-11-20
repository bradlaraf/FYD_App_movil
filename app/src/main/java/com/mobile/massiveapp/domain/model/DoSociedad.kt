package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.SociedadEntity
import com.mobile.massiveapp.data.model.Sociedad

data class DoSociedad(
    val AliasName: String,
    val CompnyName: String,
    val E_Mail: String,
    val TaxIdNum: String,
    val WebSite: String
)

fun SociedadEntity.toDomain() = DoSociedad(
    AliasName = AliasName,
    CompnyName = CompnyName,
    E_Mail = E_Mail,
    TaxIdNum = TaxIdNum,
    WebSite = WebSite
)

fun Sociedad.toDomain() = DoSociedad(
    AliasName = AliasName,
    CompnyName = CompnyName,
    E_Mail = E_Mail,
    TaxIdNum = TaxIdNum,
    WebSite = WebSite
)
