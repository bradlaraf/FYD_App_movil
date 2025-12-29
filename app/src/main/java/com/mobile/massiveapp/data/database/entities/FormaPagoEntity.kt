package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.FormaPago

@Entity(tableName = "FormaPago")
data class FormaPagoEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "U_MSV_MA_CUENTA") val U_MSV_MA_CUENTA: String
)

fun FormaPago.toDatabase() = FormaPagoEntity(
    Code = Code,
    Name = Name,
    U_MSV_MA_CUENTA = U_MSV_MA_CUENTA
)

