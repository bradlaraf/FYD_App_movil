package com.mobile.massiveapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.massiveapp.data.database.entities.ConfigurarUsuariosEntity
import com.mobile.massiveapp.domain.model.DoConfigurarUsuario
import com.mobile.massiveapp.domain.model.DoConfigurarUsuarioInfo
import com.mobile.massiveapp.domain.model.DoValidarUsuario
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigurarUsuarioDao:BaseDao<ConfigurarUsuariosEntity> {

    @Query("SELECT * FROM ConfigurarUsuarios")
    suspend fun getAll(): List<ConfigurarUsuariosEntity>

    @Query("SELECT * FROM ConfigurarUsuarios WHERE ResetIdPhone == 'N'")
    suspend fun getAllUsersWithNoResetId(): List<ConfigurarUsuariosEntity>

    @Query("""
        SELECT 
            T0.AccStatusSession,
            T0.Code,
            T0.Name,
            T0.Email,
            T0.SuperUser,
            T0.DefaultSlpCode,
            T0.Password,
            T0.AccLocked,
            T0.IdPhone1,
            T0.CanApprove,
            T0.CanCreate,
            T0.CanDecline,
            T0.CanEditPrice,
            T0.CanUpdate,
            T0.Phone1,
            T0.DefaultOrderSeries,
            T0.DefaultPagoRSeries,
            T0.DefaultSNSerieCli,
            T0.DefaultPriceList,
            T0.DefaultWarehouse,
            T0.DefaultCurrency,
            T0.DefaultTaxCode,
            T0.DefaultProyecto,
            T0.DefaultAcctCodeCh,
            T0.DefaultAcctCodeDe,
            T0.DefaultAcctCodeEf,
            T0.DefaultAcctCodeTr,
            (SELECT COUNT(*) FROM UsuarioListaPrecios WHERE Code = T0.Code AND AccLocked = "N") as ListaPrecios,
            (SELECT COUNT(*) FROM UsuarioGrupoArticulos WHERE Code = T0.Code AND AccLocked = "N") as GrupoArticulos,
            (SELECT COUNT(*) FROM UsuarioGrupoSocios WHERE Code = T0.Code AND AccLocked = "N") as GrupoSocios,
            (SELECT COUNT(*) FROM UsuarioAlmacenes WHERE Code = T0.Code AND AccLocked = "N") as Almacenes,
            (SELECT COUNT(*) FROM UsuarioZonas WHERE Code = T0.Code AND AccLocked = "N") as Zonas
        FROM ConfigurarUsuarios T0
        ORDER BY T0.Name, T0.Code
    """)
    fun getAllCUsuariosFlow(): Flow<List<DoConfigurarUsuario>>

    @Query("""
        SELECT 
            * 
        FROM ConfigurarUsuarios
        WHERE Code = :userCode
    """)
    suspend fun getUser(userCode: String): ConfigurarUsuariosEntity

    @Query("""
        SELECT 
            CASE WHEN :Codigo IN (SELECT Z0.Code FROM ConfigurarUsuarios Z0)  THEN 1 ELSE 0 END AS "Codigo",
            CASE WHEN :Email IN (SELECT Z0.Email FROM ConfigurarUsuarios Z0)  THEN 1 ELSE 0 END AS "Email",
            CASE WHEN :IdTelefono IN (SELECT Z0.IdPhone1 FROM ConfigurarUsuarios Z0)  THEN 1 ELSE 0 END AS "IdTelefono",
            CASE WHEN :Vendedor IN (SELECT Z0.DefaultSlpCode FROM ConfigurarUsuarios Z0)  THEN 1 ELSE 0 END AS "Vendedor",
            CASE WHEN :Telefono IN (SELECT Z0.Phone1 FROM ConfigurarUsuarios Z0)  THEN 1 ELSE 0 END AS "Telefono",
            CASE WHEN :Nombre IN (SELECT Z0.Name FROM ConfigurarUsuarios Z0)  THEN 1 ELSE 0 END AS "Nombre"
        FROM ConfigurarUsuarios
        LIMIT 1
    """)
    suspend fun validarUsuario(Codigo:String, Email: String, IdTelefono:String, Vendedor:String, Telefono:String, Nombre:String): DoValidarUsuario


    @Query("""
        SELECT 
            T0.AccStatusSession,
            T0.Code,
            T0.Password,
            T0.IdPhone1,
            T0.Name,
            T0.Email,
            T0.Phone1, 
            T1.SlpName as DefaultSlpCode,
            (SELECT SeriesName FROM SeriesN WHERE Series = T0.DefaultOrderSeries) as DefaultOrderSeries,
            (SELECT SeriesName FROM SeriesN WHERE Series = T0.DefaultPagoRSeries) as DefaultPagoRSeries,
            (SELECT SeriesName FROM SeriesN WHERE Series = T0.DefaultSNSerieCli) as DefaultSNSerieCli,
            T2.ListName as DefaultPriceList,
            T3.WhsName as DefaultWarehouse,
            T0.DefaultCurrency,
            (SELECT Name FROM Impuesto WHERE Code = T0.DefaultTaxCode) as DefaultTaxCode,
            IFNULL((SELECT PrjName FROM Proyecto WHERE PrjCode = T0.DefaultProyecto) ,'') as DefaultProyecto,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeCh) ,'') as DefaultAcctCodeCh,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeDe) ,'') as DefaultAcctCodeDe,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeEf) ,'') as DefaultAcctCodeEf,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeTr) ,'') as DefaultAcctCodeTr,
            T0.SuperUser,
            T0.AccLocked,
            T0.CanApprove,
            T0.CanCreate,
            T0.CanDecline,
            T0.CanEditPrice,
            T0.CanUpdate,
            (SELECT COUNT(*) FROM UsuarioListaPrecios WHERE Code = T0.Code AND AccLocked = "N") as ListaPrecios,
            (SELECT COUNT(*) FROM UsuarioGrupoArticulos WHERE Code = T0.Code AND AccLocked = "N") as GrupoArticulos,
            (SELECT COUNT(*) FROM UsuarioGrupoSocios WHERE Code = T0.Code AND AccLocked = "N") as GrupoSocios,
            (SELECT COUNT(*) FROM UsuarioAlmacenes WHERE Code = T0.Code AND AccLocked = "N") as Almacenes,
            (SELECT COUNT(*) FROM UsuarioZonas WHERE Code = T0.Code AND AccLocked = "N") as Zonas
        FROM ConfigurarUsuarios T0
        INNER JOIN Vendedor T1 ON T0.DefaultSlpCode = T1.SlpCode 
        INNER JOIN ListaPrecio T2 ON T2.ListNum = T0.DefaultPriceList
        INNER JOIN Almacenes T3 ON T3.WhsCode = T0.DefaultWarehouse
        WHERE Code = :userCode
    """)
    suspend fun getUsuarioInfo(userCode:String):DoConfigurarUsuario

    @Query("""
        SELECT 
            T0.Code,
            T0.Password,
            T0.IdPhone1,
            T0.Name,
            T0.Email,
            T0.Phone1, 
            T0.DefaultSlpCode,
            T0.DefaultOrderSeries,
            T0.DefaultPagoRSeries,
            T0.DefaultSNSerieCli,
            T0.DefaultPriceList,
            T0.DefaultWarehouse,
            T0.DefaultCurrency,
            T0.DefaultTaxCode,
            T0.DefaultProyecto,
            T0.DefaultAcctCodeCh,
            T0.DefaultAcctCodeDe,
            T0.DefaultAcctCodeEf,
            T0.DefaultAcctCodeTr,
            T0.SuperUser,
            T0.AccLocked,
            T0.CanApprove,
            T0.CanCreate,
            T0.CanDecline,
            T0.CanEditPrice,
            T0.CanUpdate,
            T1.SlpName as NombreVendedor,
            (SELECT SeriesName FROM SeriesN WHERE Series = T0.DefaultOrderSeries) as SeriePedido,
            (SELECT SeriesName FROM SeriesN WHERE Series = T0.DefaultPagoRSeries) as SeriePago,
            (SELECT SeriesName FROM SeriesN WHERE Series = T0.DefaultSNSerieCli) as SerieSocio,
            T2.ListName as ListaPrecio,
            T3.WhsName as Almacen,
            T0.DefaultCurrency as Moneda,
            (SELECT Name FROM Impuesto WHERE Code = T0.DefaultTaxCode) as Impuesto,
            IFNULL((SELECT PrjName FROM Proyecto WHERE PrjCode = T0.DefaultProyecto) ,'') as Proyecto,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeCh) ,'') as CuentaCheque,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeDe) ,'') as CuentaDeposito,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeEf) ,'') as CuentaEfectivo,
            IFNULL((SELECT AcctName FROM CuentasC WHERE AcctCode = T0.DefaultAcctCodeTr) ,'') as CuentaTransferencia,
            T0.AccStatusSession
        FROM ConfigurarUsuarios T0
        INNER JOIN Vendedor T1 ON T0.DefaultSlpCode = T1.SlpCode 
        INNER JOIN ListaPrecio T2 ON T2.ListNum = T0.DefaultPriceList
        INNER JOIN Almacenes T3 ON T3.WhsCode = T0.DefaultWarehouse
        WHERE Code = :userCode
    """)
    suspend fun getUsuarioInfoCodes(userCode:String): DoConfigurarUsuarioInfo

    @Query("DELETE FROM ConfigurarUsuarios")
    suspend fun clearAll()

    @Transaction


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ConfigurarUsuariosEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ConfigurarUsuariosEntity)

    @Update
    suspend fun update(items: List<ConfigurarUsuariosEntity>)

}