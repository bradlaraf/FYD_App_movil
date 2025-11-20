package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Banco

@Entity(tableName = "Banco")
data class BancoEntity(
    @PrimaryKey
    @ColumnInfo(name = "AbsEntry") val AbsEntry: Int,
    @ColumnInfo(name = "BankCode") val BankCode: String,
    @ColumnInfo(name = "BankName") val BankName: String,
    @ColumnInfo(name = "CountryCod") val CountryCod: String
)

fun Banco.toDatabase() = BancoEntity(
    AbsEntry = AbsEntry,
    BankCode = BankCode,
    BankName = BankName,
    CountryCod = CountryCod
)

fun BancoEntity.toModel() = Banco(
    AbsEntry = AbsEntry,
    BankCode = BankCode,
    BankName = BankName,
    CountryCod = CountryCod
)