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
)

fun FormaPago.toDatabase() = FormaPagoEntity(
    Code = Code,
    Name = Name
)

