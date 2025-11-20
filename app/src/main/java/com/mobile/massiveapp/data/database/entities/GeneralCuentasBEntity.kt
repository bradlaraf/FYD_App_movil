package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralCuentasB

@Entity(tableName = "CuentaBancaria")
data class GeneralCuentasBEntity(
    @PrimaryKey
    @ColumnInfo(name = "AbsEntry") val AbsEntry: Int,
    @ColumnInfo(name = "Account") val Account: String,
    @ColumnInfo(name = "BankCode") val BankCode: String,
    @ColumnInfo(name = "BankKey") val BankKey: Int,
    @ColumnInfo(name = "Branch") val Branch: String,
    @ColumnInfo(name = "GLAccount") val GLAccount: String
)

fun GeneralCuentasB.toDatabase() = GeneralCuentasBEntity(
    AbsEntry = AbsEntry,
    Account = Account,
    BankCode = BankCode,
    BankKey = BankKey,
    Branch = Branch,
    GLAccount = GLAccount
)

fun GeneralCuentasBEntity.toModel() = GeneralCuentasB(
    AbsEntry = AbsEntry,
    Account = Account,
    BankCode = BankCode,
    BankKey = BankKey,
    Branch = Branch,
    GLAccount = GLAccount
)

