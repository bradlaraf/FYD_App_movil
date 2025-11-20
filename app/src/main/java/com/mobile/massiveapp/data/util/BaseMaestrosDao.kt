package com.mobile.massiveapp.data.util

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

abstract class BaseMaestrosDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: T)

    @Delete
    abstract suspend fun delete(entity: T)

}