package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.ConfiguracionEntity

@Dao
interface ConfiguracionDao {

    @Query("SELECT * FROM Configuracion")
    suspend fun getAll(): ConfiguracionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(configuracion: ConfiguracionEntity)

    @Query("DELETE FROM Configuracion")
    suspend fun clearAll()

    @Query("""
        UPDATE Configuracion
        SET UsarLimites = :usarLimite,
            TopArticulo = :articulo,
            TopFactura = :factura,
            TopCliente = :cliente
    """)
    suspend fun updateLimites(usarLimite: Boolean ,articulo: Int, factura: Int, cliente: Int)

}