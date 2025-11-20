package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ClientePagosEntity

@Dao
interface ClientePagosDao:BaseDao<ClientePagosEntity> {

    @Query("SELECT * FROM ClientePagos ORDER BY CardName, TypePayment")
    suspend fun getAll(): List<ClientePagosEntity>

    @Query("""
        SELECT 
            * 
        FROM ClientePagos
        WHERE DocDate = :fecha
        ORDER BY CardName, TypePayment""")
    suspend fun getAll(fecha: String): List<ClientePagosEntity>


        //Traer todos los pagos cabecera Migrados
    @Query("""
        SELECT 
        * 
        FROM ClientePagos 
        WHERE AccMigrated = 'Y' AND DocDate = :fechaActual
    """)
    suspend fun getAllPedidosCabeceraMigrados(fechaActual: String): List<ClientePagosEntity>

    @Query("""
        SELECT
            * 
        FROM ClientePagos
        WHERE Canceled = 'Y' AND DocDate = :fechaActual
    """)
    suspend fun getPagosCancelados(fechaActual: String): List<ClientePagosEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ClientePagosEntity>)


    @Update
    suspend fun updateList(items: List<ClientePagosEntity>)
    @Query("UPDATE ClientePagos SET Canceled = :canceled WHERE AccDocEntry = :accDocEntry")
    suspend fun updatePagoCanceled(canceled: String, accDocEntry: String)


    @Query("DELETE FROM ClientePagos")
    suspend fun clearAll()


    //Guardar una cabecera de cobranza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUnaCobranza(cobranza: ClientePagosEntity)

    //Traer todos los pagos No Migrados
    @Query("""
        SELECT 
            * 
        FROM ClientePagos 
        WHERE AccMigrated = 'N' AND DocDate = :fechaActual
    """)
    suspend fun getAllPedidosCabeceraNoMigrados(fechaActual:String): List<ClientePagosEntity>

        //Obtener todos los pagos sin migrar
    @Query("SELECT * FROM ClientePagos WHERE AccMigrated = 'N'")
    suspend fun getAllPagosSinMigrar(): List<ClientePagosEntity>


    @Query("SELECT * FROM ClientePagos WHERE AccMigrated = 'N' AND DocDate = :fechaActual")
    suspend fun getAllPagosNoMigradosFechaActual(fechaActual: String): List<ClientePagosEntity>

        //Obtener un pedido cabecera por AccDocEntry
    @Query("SELECT * FROM ClientePagos WHERE AccDocEntry = :accDocEntry")
    suspend fun getPagoCabeceraPorAccDocEntry(accDocEntry: String): ClientePagosEntity

    @Query("DELETE FROM ClientePagos\n" +
            "WHERE DocDate NOT IN (:fechaActual)")
    suspend fun deleteAllPagosFueraDeFechaActual(fechaActual: String)
}