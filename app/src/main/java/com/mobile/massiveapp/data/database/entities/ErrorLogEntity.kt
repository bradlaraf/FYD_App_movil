package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "ErrorLog", primaryKeys = ["ErrorDate", "ErrorHour"])
data class ErrorLogEntity(
    @ColumnInfo("ErrorCode") val ErrorCode: String,
    @ColumnInfo("ErrorMessage") val ErrorMessage: String,
    @ColumnInfo("ErrorDate") val ErrorDate: String,
    @ColumnInfo("ErrorHour") val ErrorHour: String
)
