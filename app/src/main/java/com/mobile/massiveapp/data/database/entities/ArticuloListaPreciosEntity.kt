package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.ArticuloListaPrecios
import com.mobile.massiveapp.domain.model.DoArticuloListaPrecios

@Entity(tableName = "ListaPrecio")
data class ArticuloListaPreciosEntity(
    @PrimaryKey
    @ColumnInfo(name = "ListNum") val ListNum: Int,
    @ColumnInfo(name = "ListName") val ListName: String,
    @ColumnInfo(name = "PrimCurr") val PrimCurr: String,
    @ColumnInfo(name = "AddCurr1") val AddCurr1: String,
    @ColumnInfo(name = "AddCurr2") val AddCurr2: String,
    @ColumnInfo(name = "IsGrossPrc") val IsGrossPrc: String
)

fun DoArticuloListaPrecios.toDatabase() = ArticuloListaPreciosEntity(
    AddCurr1 = AddCurr1,
    AddCurr2 = AddCurr2,
    IsGrossPrc = IsGrossPrc,
    ListName = ListName,
    ListNum = ListNum,
    PrimCurr = PrimCurr
)

fun ArticuloListaPrecios.toDatabase() = ArticuloListaPreciosEntity(
    AddCurr1 = AddCurr1,
    AddCurr2 = AddCurr2,
    IsGrossPrc = IsGrossPrc,
    ListName = ListName,
    ListNum = ListNum,
    PrimCurr = PrimCurr
)
