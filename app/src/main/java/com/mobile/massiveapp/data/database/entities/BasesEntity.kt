package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Bases


@Entity("Bases")
data class BasesEntity(
    @PrimaryKey
    @ColumnInfo("Code") val Code: String,
    @ColumnInfo("CompnyName") val CompnyName: String,
    @ColumnInfo("DataBase") val DataBase: String
)

fun Bases.toDatabase() = BasesEntity(
    Code = Code,
    CompnyName = CompnyName,
    DataBase = DataBase
)

fun BasesEntity.toModel() = Bases(
    Code = Code,
    CompnyName = CompnyName,
    DataBase = DataBase
)