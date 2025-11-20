package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ClientEntity

@Dao
interface ClienteDao {
    @Query("SELECT * FROM Cliente_pedido ORDER BY CardName DESC")
    suspend fun getAllClients(): List<ClientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertAll(clients:List<ClientEntity>)

    @Query("DELETE FROM Cliente_pedido")
    suspend fun clearAllClients()


        //Get INFO CLIENTE



}