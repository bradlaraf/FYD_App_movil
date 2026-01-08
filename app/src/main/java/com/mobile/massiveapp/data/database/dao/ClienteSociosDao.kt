package com.mobile.massiveapp.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ClienteSociosEntity
import com.mobile.massiveapp.domain.model.DoClienteInfo
import com.mobile.massiveapp.domain.model.DoClienteScreen
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteSociosDao:BaseDao<ClienteSociosEntity> {
    @Query("""
        SELECT
         T0.CardCode,
         T0.CardName,
         T0.AccMigrated,
         T0.AccFinalized,
         T0.LicTradNum,
         T0.ListNum
        FROM SocioNegocio T0
        WHERE T0.AccMigrated = 'Y' AND AccLocked = 'N' AND CardName LIKE '%' || :text || '%' OR LicTradNum LIKE '%' || :text || '%'
        ORDER BY AccFinalized,CardName, CardCode 
    """)
    suspend fun searchSociosView(text: String): List<DoClienteScreen>
    @Query("""
        SELECT
         T0.CardCode,
         T0.CardName,
         T0.AccMigrated,
         T0.AccFinalized,
         T0.LicTradNum,
         T0.ListNum
        FROM SocioNegocio T0
        WHERE T0.AccMigrated = 'Y' 
            AND AccLocked = 'N'
            AND CardType = 'C'
        ORDER BY AccFinalized,CardName, CardCode
    """)
    suspend fun getAllSociosScreen(): List<DoClienteScreen>

    @Query("""
        SELECT
         T0.CardCode,
         T0.CardName,
         T0.AccMigrated,
         T0.AccFinalized,
         T0.LicTradNum,
         T0.ListNum
        FROM SocioNegocio T0
        WHERE AccLocked = 'N'
        ORDER BY AccFinalized,CardName, CardCode
    """)
    suspend fun getAll(): List<DoClienteScreen>

    @Query("""
        SELECT 
            T0.CardCode,
            T0.CardName,
            T0.AccMigrated,
            T0.AccFinalized,
            T0.LicTradNum,
            T0.ListNum
        FROM SocioNegocio T0
        WHERE CardCode IN (SELECT DISTINCT 
                            Z0.CardCode 
                            FROM Factura Z0 
                            WHERE Z0.PaidToDate > 0.0 AND Z0.SlpCode IN (SELECT DefaultSlpCode FROM Usuario) 
                            )
        ORDER BY CardName""")
    suspend fun getSociosConFacturaPendiente(): List<DoClienteScreen>

    @Query("""
        SELECT 
            T0.CardCode,
            T0.CardName,
            T0.AccMigrated,
            T0.AccFinalized,
            T0.LicTradNum,
            T0.ListNum
        FROM SocioNegocio T0
        WHERE CardCode IN (SELECT DISTINCT 
                            Z0.CardCode 
                            FROM Factura Z0 
                            WHERE Z0.PaidToDate > 0.0 
                            )
        ORDER BY CardName""")
    suspend fun getSociosConFacturaPendienteSuperuser(): List<DoClienteScreen>

    @Query("""
        DELETE FROM SocioDirecciones
        WHERE CardCode NOT IN (SELECT CardCode FROM SocioNegocio)
    """)
    suspend fun deleteAllDirecSinCabecera()

    @Query("""
        DELETE FROM SocioContactos
        WHERE CardCode NOT IN (SELECT CardCode FROM SocioNegocio)
    """)
    suspend fun deleteAllContacSinCabecera()

    //Cantidad de CLientes
    @Query("SELECT COUNT(CardCode) FROM SocioNegocio")
    fun getCountClientes():Flow<Int>

    //Todos los socios con PAGING
    @Query("SELECT * FROM SocioNegocio WHERE AccLocked == 'N' ORDER BY CardName LIMIT :pageSize OFFSET :offset")
    fun getAllSociosPaging(pageSize: Int, offset: Int): List<ClienteSociosEntity>

    @Query("SELECT * FROM SocioNegocio WHERE AccLocked == 'N' ORDER BY CardName")
    fun getAllSociosPagingSource(): PagingSource<Int, ClienteSociosEntity>

    @Query("SELECT * FROM SocioNegocio ORDER BY CardName")
    suspend fun getAllSocios(): List<ClienteSociosEntity>


    @Query("SELECT * FROM SocioNegocio WHERE AccMigrated = 'N'")
    suspend fun getAllSociosNoMigrados(): List<ClienteSociosEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSocios(socios: List<ClienteSociosEntity>)

    @Query("SELECT * FROM SocioNegocio WHERE CardCode = :cardCode")
    suspend fun getInfoSocio(cardCode: String): ClienteSociosEntity


    //Eliminar un socio de negocio por CardCode
    @Query("DELETE FROM SocioNegocio WHERE CardCode = :cardCode")
    suspend fun deleteSocioPorCardCode(cardCode: String)


    @Update
    suspend fun updateSocios(socios: List<ClienteSociosEntity>)


    //Revisar si se usa o no
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocio(socio: ClienteSociosEntity)


    @Query("DELETE FROM SocioNegocio")
    suspend fun clearAllSocios()


    //Update de el socio
    @Query("UPDATE SocioNegocio SET CardName = :cardName WHERE CardCode = :cardCode")
    suspend fun updateSocio(cardCode: String, cardName: String)


    //Obtener un cliente socio por CardCode
    @Query("SELECT * FROM SocioNegocio WHERE CardCode = :cardCode")
    suspend fun getClienteSocioPorCardCode(cardCode: String): ClienteSociosEntity




        //Obtener todos los socios de negocio con Facturas
    @Query("""
        SELECT 
            * 
        FROM SocioNegocio 
        WHERE CardCode IN (SELECT DISTINCT CardCode FROM Factura) 
        ORDER BY CardName""")
    suspend fun getAllSocioNegocioConFacturas(): List<ClienteSociosEntity>


        //Obtener todos los SN MIGRADOS
    @Query("""
        SELECT 
            * 
        FROM SocioNegocio 
        WHERE CardType = "L"
        ORDER BY CardName
        """)
    suspend fun getAllSocioFiltradoPorMigrado(): List<ClienteSociosEntity>





        //Obtener la info del Cliente
        @Query("""
            SELECT 
                IFNULL(T0.CardName, '--') AS CardName,
                IFNULL(T0.CardCode, '--') AS CardCode,
                IFNULL(T0.CardType, '--') AS CardType,
                IFNULL(T0.U_MSV_LO_TIPOPER, '--') AS U_MSV_LO_TIPOPER,
                IFNULL(T0.U_MSV_LO_TIPODOC, '--') AS U_MSV_LO_TIPODOC,
                IFNULL(T0.LicTradNum, '--') AS LicTradNum,
                IFNULL(T0.U_MSV_LO_APELPAT, '--') AS U_MSV_LO_APELPAT,
                IFNULL(T0.U_MSV_LO_APELMAT, '--') AS U_MSV_LO_APELMAT,
                IFNULL(T0.U_MSV_LO_PRIMNOM, '--') AS U_MSV_LO_PRIMNOM,
                IFNULL(T0.U_MSV_LO_SEGUNOM, '--') AS U_MSV_LO_SEGUNOM,
                IFNULL(T1.GroupName, '--') AS GroupName,
                IFNULL(T0.Currency, '--') AS Currency,
                IFNULL(T0.Phone1, '--') AS Phone1,
                IFNULL(T0.Phone2, '--') AS Phone2,
                IFNULL(T0.Cellular, '--') AS Cellular,
                IFNULL(T2.PymntGroup, '--') AS PymntGroup,
                IFNULL(T3.ListName, '--') AS ListName,
                IFNULL(T0.E_Mail, ' ') AS E_Mail,
                IFNULL(T4.Name, '--') AS 'Indicador',
                IFNULL(T5.Name, '--') AS 'Zona'
            FROM SocioNegocio T0
            LEFT JOIN GrupoSocio T1 ON T0.GroupCode = T1.GroupCode
            LEFT JOIN CondicionPago T2 ON T0.GroupNum = T2.GroupNum
            LEFT JOIN ListaPrecio T3 ON T0.ListNum = T3.ListNum
            LEFT JOIN Indicador T4 ON T0.Indicator = T4.Code
            LEFT JOIN Zona T5 ON T5.Code = :zoneCode
            WHERE T0.CardCode = :cardCode
        """)
    suspend fun getInfoCliente(cardCode: String, zoneCode: String): DoClienteInfo
}