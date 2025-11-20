package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.GeneralActividadesE

@Entity(tableName = "ActividadesE")
data class GeneralActividadesEEntity(
    @PrimaryKey
    @ColumnInfo(name = "Code") val Code: String,
    @ColumnInfo(name = "Name") val Name: String
)


fun GeneralActividadesE.toDatabase() = GeneralActividadesEEntity(
    Code = Code,
    Name = Name
)
