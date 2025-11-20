package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface

data class SeriesN(
    val ObjectCode: Int,
    val Series: Int,
    val SeriesName: String
):MappingInteface<SeriesN>() {

    constructor() : this(
        ObjectCode = 0,
        Series = 0,
        SeriesName = ""
    )

    override fun map(data: List<SeriesN>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<SeriesN>): List<Any> {
        return emptyList()
    }
}