package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.BancoEntity

@Dao
interface BancoDao:BaseDao<BancoEntity> {

    @Query("""SELECT * FROM Banco""")
    suspend fun getAll():List<BancoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(banco: BancoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bancos: List<BancoEntity>)

    @Query("""DELETE FROM Banco""")
    suspend fun delete()
}