package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralCondicionesEntity

@Dao
interface GeneralCondicionesDao:BaseDao<GeneralCondicionesEntity> {

    @Query("SELECT * FROM CondicionPago")
    suspend fun getAll(): List<GeneralCondicionesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalCondiciones: List<GeneralCondicionesEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalCondiciones: GeneralCondicionesEntity)

    @Query("DELETE FROM CondicionPago")
    suspend fun clearAll()

    @Update
    suspend fun update(generalCondiciones: List<GeneralCondicionesEntity>)

        //Obtener la condicion de pago por defatult
    @Query("""
        SELECT 
            * 
        FROM CondicionPago 
        WHERE GroupNum IN (
            SELECT GroupNum FROM SocioNegocio WHERE CardCode = :cardCode
        )""")
    suspend fun getCondicionDePagoDefault(cardCode: String): GeneralCondicionesEntity

    @Query("""
            SELECT 
                * 
            FROM CondicionPago 
            WHERE GroupNum = :groupNum
            """)
    suspend fun getCondicionDePago(groupNum:Int): GeneralCondicionesEntity

    @Query("""
        SELECT            
            T0.* 
        FROM CondicionPago T0        
        WHERE T0.GroupNum IN (
            -1, 
            (SELECT 
                Z0.GroupNum 
             FROM CondicionPago Z0 
             INNER JOIN SocioNegocio Z1 ON Z0.GroupNum = Z1.GroupNum 
             WHERE Z1.CardCode = :cardCode)
            )
    """)
    suspend fun getCondicionesDisponibles(cardCode: String): List<GeneralCondicionesEntity>
}