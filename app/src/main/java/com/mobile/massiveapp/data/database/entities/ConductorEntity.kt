package com.mobile.massiveapp.data.database.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Conductor")
data class ConductorEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "U_MSV_MA_CONLIC") val U_MSV_MA_CONLIC: String,
    @ColumnInfo(name = "U_MSV_MA_COTDOC") val U_MSV_MA_COTDOC: String,
    @ColumnInfo(name = "U_MSV_MA_CONNDOC") val U_MSV_MA_CONNDOC: String,
    @ColumnInfo(name = "U_MSV_MA_CONOMB") val U_MSV_MA_CONOMB: String,
    @ColumnInfo(name = "U_MSV_MA_EMTDOC") val U_MSV_MA_EMTDOC: String,
    @ColumnInfo(name = "U_MSV_MA_EMNNDOC") val U_MSV_MA_EMNNDOC: String,
    @ColumnInfo(name = "U_MSV_MA_EMNOMB") val U_MSV_MA_EMNOMB: String,
    @ColumnInfo(name = "U_MSV_MA_MTC") val U_MSV_MA_MTC: String
)
