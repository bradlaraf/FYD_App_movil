package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.SeriesN
import com.mobile.massiveapp.domain.model.DoSeriesN

@Entity(tableName = "SeriesN")
data class SeriesNEntity(
    @PrimaryKey
    @ColumnInfo(name = "ObjectCode") val ObjectCode: Int,
    @ColumnInfo(name = "Series") val Series: Int,
    @ColumnInfo(name = "SeriesName") val SeriesName: String
)

fun SeriesN.toDatabase() = SeriesNEntity(
    ObjectCode = ObjectCode,
    Series = Series,
    SeriesName = SeriesName
)

fun SeriesNEntity.toDomain() = DoSeriesN(
    ObjectCode = ObjectCode,
    Series = Series,
    SeriesName = SeriesName
)