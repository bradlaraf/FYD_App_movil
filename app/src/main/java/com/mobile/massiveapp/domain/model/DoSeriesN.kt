package com.mobile.massiveapp.domain.model

data class DoSeriesN (
    val ObjectCode: Int,
    val Series: Int,
    val SeriesName: String
){
    constructor(): this(
        ObjectCode = 0,
        Series = 0,
        SeriesName = ""
    )
}

