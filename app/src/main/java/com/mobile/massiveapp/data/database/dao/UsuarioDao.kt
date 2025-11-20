package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.massiveapp.data.database.entities.UsuarioEntity

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM Usuario LIMIT 1")
    suspend fun getAll(): UsuarioEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity)

    @Query("INSERT INTO Usuario (\n" +
            "    Code,\n" +
            "    Comment,\n" +
            "    DefaultCC1,\n" +
            "    DefaultCC2,\n" +
            "    DefaultCC3,\n" +
            "    DefaultCC4,\n" +
            "    DefaultCC5,\n" +
            "    DefaultCurrency,\n" +
            "    DefaultEmpId,\n" +
            "    DefaultOrderSeries,\n" +
            "    DefaultPagoRSeries,\n" +
            "    DefaultPriceList,\n" +
            "    DefaultAcctCodeEf,\n" +
            "    DefaultAcctCodeTr,\n" +
            "    DefaultProyecto,\n" +
            "    DefaultSNSerieCli,\n" +
            "    DefaultSlpCode,\n" +
            "    DefaultTaxCode,\n" +
            "    DefaultWarehouse,\n" +
            "    Email,\n" +
            "    IdPhone1,\n" +
            "    IdPhone1Val,\n" +
            "    Image,\n" +
            "    Name,\n" +
            "    Password,\n" +
            "    Phone1,\n" +
            "    ShowImage,\n" +
            "    SuperUser\n" +
            ") VALUES (\n" +
            "    'fcruz',\n" +
            "    '',\n" +
            "    '',\n" +
            "    '',\n" +
            "    '',\n" +
            "    '',\n" +
            "    '',\n" +
            "    'SOL',\n" +
            "    5,\n" +
            "    13,\n" +
            "    21,\n" +
            "    1,\n" +
            "    '101101',\n" +
            "    '104101',\n" +
            "    '',\n" +
            "    1,\n" +
            "    1,\n" +
            "    'EXO',\n" +
            "    'APRI.001',\n" +
            "    'fcruz@massive.pe',\n" +
            "    '',\n" +
            "    'N',\n" +
            "    '',\n" +
            "    'CRUZ MONTESINOS, FRANCO',\n" +
            "    '1234',\n" +
            "    '986954982',\n" +
            "    'N',\n" +
            "    'N'\n" +
            ")")
    suspend fun insertUsuarioDefault()

    @Query("UPDATE Usuario SET CanCreate = 'Y'")
    suspend fun changeCanCreate()
    @Query("DELETE FROM Usuario")
    suspend fun deleteAll()

}