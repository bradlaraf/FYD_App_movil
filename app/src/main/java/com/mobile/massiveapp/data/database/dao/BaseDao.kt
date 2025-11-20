package com.mobile.massiveapp.data.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllData(data: List<T>)
}

interface Mapping<T, Y>{

}