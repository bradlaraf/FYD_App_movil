package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.GeneralCuentasBEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GeneralCuentasBDao:BaseDao<GeneralCuentasBEntity> {

    @Query("SELECT * FROM CuentaBancaria")
    suspend fun getAll(): List<GeneralCuentasBEntity>

    @Query("""
            SELECT 
                DefaultAcctCodeTr
            FROM Usuario 
            """)
    fun getCuentaTransferenciaDefault(): Flow<String>

    @Query("""
            SELECT 
                DefaultAcctCodeEf 
            FROM Usuario  
            """)
    fun getCuentaCashDefault(): Flow<String>

    @Query("""
            SELECT 
                DefaultAcctCodeCh 
            FROM Usuario  
            """)
    fun getCuentaChequeDefault(): Flow<String>

    @Query("""
            SELECT 
                DefaultAcctCodeDe 
            FROM Usuario  
            """)
    fun getCuentaDepositoDefault(): Flow<String>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(generalCuentasBEntity: List<GeneralCuentasBEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(generalCuentasBEntity: GeneralCuentasBEntity)

    @Query("DELETE FROM CuentaBancaria")
    suspend fun clearAll()

    @Update
    suspend fun update(generalCuentasBEntity: List<GeneralCuentasBEntity>)
}