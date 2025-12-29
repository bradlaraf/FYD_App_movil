package com.mobile.massiveapp.domain.model

import com.mobile.massiveapp.data.database.entities.ConductorEntity

data class DoConductor(
    val Code: String,
    val Name: String,
    val U_MSV_MA_CONLIC: String,
    val U_MSV_MA_COTDOC: String,
    val U_MSV_MA_CONNDOC: String,
    val U_MSV_MA_CONOMB: String,
    val U_MSV_MA_EMTDOC: String,
    val U_MSV_MA_EMNNDOC: String,
    val U_MSV_MA_EMNOMB: String,
    val U_MSV_MA_MTC: String
)

fun ConductorEntity.toDomain() = DoConductor(
    Code = Code,
    Name = Name,
    U_MSV_MA_CONLIC = U_MSV_MA_CONLIC,
    U_MSV_MA_COTDOC = U_MSV_MA_COTDOC,
    U_MSV_MA_CONNDOC = U_MSV_MA_CONNDOC,
    U_MSV_MA_CONOMB = U_MSV_MA_CONOMB,
    U_MSV_MA_EMTDOC = U_MSV_MA_EMTDOC,
    U_MSV_MA_EMNNDOC = U_MSV_MA_EMNNDOC,
    U_MSV_MA_EMNOMB = U_MSV_MA_EMNOMB,
    U_MSV_MA_MTC = U_MSV_MA_MTC
)
