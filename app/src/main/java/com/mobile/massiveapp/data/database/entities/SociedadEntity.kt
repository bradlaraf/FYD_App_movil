package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Sociedad
import com.mobile.massiveapp.domain.model.DoSociedad

@Entity(tableName = "Sociedad")
data class SociedadEntity(
    @PrimaryKey
    @ColumnInfo(name = "TaxIdNum") val TaxIdNum: String,
    @ColumnInfo(name = "CompnyName") val CompnyName: String,
    @ColumnInfo(name = "AliasName") val AliasName: String,
    @ColumnInfo(name = "E_Mail") val E_Mail: String,
    @ColumnInfo(name = "WebSite") val WebSite: String,
    @ColumnInfo(name = "PriceDec") val PriceDec: Int
)

fun Sociedad.toDatabase() = SociedadEntity(
    TaxIdNum = TaxIdNum,
    CompnyName = CompnyName,
    AliasName = AliasName,
    E_Mail = E_Mail,
    WebSite = WebSite,
    PriceDec = PriceDec
)

fun DoSociedad.toDatabase() = SociedadEntity(
    TaxIdNum = TaxIdNum,
    CompnyName = CompnyName,
    AliasName = AliasName,
    E_Mail = E_Mail,
    WebSite = WebSite,
    PriceDec = PriceDec
)