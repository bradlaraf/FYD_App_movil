package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.CuentasC
import com.mobile.massiveapp.domain.model.DoCuentasC

@Entity(tableName = "CuentasC")
data class CuentasCEntity(
    @PrimaryKey
    @ColumnInfo(name = "AcctCode") val AcctCode: String,
    @ColumnInfo(name = "AcctName") val AcctName: String
)

fun CuentasC.toDatabase() = CuentasCEntity(
    AcctCode = AcctCode,
    AcctName = AcctName
)

fun CuentasCEntity.toDomain() = DoCuentasC(
    AcctCode = AcctCode,
    AcctName = AcctName
)
