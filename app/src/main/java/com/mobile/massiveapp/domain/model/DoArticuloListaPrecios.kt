package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ArticuloListaPreciosEntity
import com.mobile.massiveapp.data.model.ArticuloListaPrecios

data class DoArticuloListaPrecios(
    val AddCurr1: String,
    val AddCurr2: String,
    val IsGrossPrc: String,
    val ListName: String,
    val ListNum: Int,
    val PrimCurr: String
)

fun ArticuloListaPrecios.toDomain() = DoArticuloListaPrecios(
    AddCurr1 = AddCurr1,
    AddCurr2 = AddCurr2,
    IsGrossPrc = IsGrossPrc,
    ListName = ListName,
    ListNum = ListNum,
    PrimCurr = PrimCurr
)

fun ArticuloListaPreciosEntity.toDomain() = DoArticuloListaPrecios(
    AddCurr1 = AddCurr1,
    AddCurr2 = AddCurr2,
    IsGrossPrc = IsGrossPrc,
    ListName = ListName,
    ListNum = ListNum,
    PrimCurr = PrimCurr
)