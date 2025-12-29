package com.mobile.massiveapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.massiveapp.data.model.Usuario

@Entity (tableName = "Usuario")
data class UsuarioEntity (
    @PrimaryKey
    @ColumnInfo("Code") val Code: String,
    @ColumnInfo("Comment") val Comment: String,
    @ColumnInfo("DefaultCC1") val DefaultCC1: String,
    @ColumnInfo("DefaultCC2") val DefaultCC2: String,
    @ColumnInfo("DefaultCC3") val DefaultCC3: String,
    @ColumnInfo("DefaultCC4") val DefaultCC4: String,
    @ColumnInfo("DefaultCC5") val DefaultCC5: String,
    @ColumnInfo("DefaultCurrency") val DefaultCurrency: String,
    @ColumnInfo("DefaultEmpId") val DefaultEmpId: Int,
    @ColumnInfo("DefaultOrderSeries") val DefaultOrderSeries: Int,
    @ColumnInfo("DefaultPagoRSeries") val DefaultPagoRSeries: Int,
    @ColumnInfo("DefaultPriceList") val DefaultPriceList: Int,
    @ColumnInfo("DefaultAcctCodeEf") val DefaultAcctCodeEf: String,
    @ColumnInfo("DefaultAcctCodeTr") val DefaultAcctCodeTr: String,
    @ColumnInfo("DefaultAcctCodeCh") val DefaultAcctCodeCh: String,
    @ColumnInfo("DefaultAcctCodeDe") val DefaultAcctCodeDe: String,
    @ColumnInfo("DefaultProyecto") val DefaultProyecto: String,
    @ColumnInfo("DefaultSNSerieCli") val DefaultSNSerieCli: Int,
    @ColumnInfo("DefaultSlpCode") val DefaultSlpCode: Int,
    @ColumnInfo("DefaultTaxCode") val DefaultTaxCode: String,
    @ColumnInfo("DefaultWarehouse") val DefaultWarehouse: String,
    @ColumnInfo("DefaultConductor") val DefaultConductor: String,
    @ColumnInfo("DefaultZona") val DefaultZona: String,
    @ColumnInfo("Email") val Email: String,
    @ColumnInfo("IdPhone1") val IdPhone1: String,
    @ColumnInfo("IdPhone1Val") val IdPhone1Val: String,
    @ColumnInfo("Image") val Image: String,
    @ColumnInfo("Name") val Name: String,
    @ColumnInfo("Password") val Password: String,
    @ColumnInfo("Phone1") val Phone1: String,
    @ColumnInfo("ShowImage") val ShowImage: String,
    @ColumnInfo("SuperUser") val SuperUser: String,
    @ColumnInfo("CanApprove") val CanApprove: String,
    @ColumnInfo("CanCreate") val CanCreate: String,
    @ColumnInfo("CanDecline") val CanDecline: String,
    @ColumnInfo("CanEditPrice") val CanEditPrice: String,
    @ColumnInfo("CanUpdate") val CanUpdate: String
)


fun Usuario.toDatabase() = UsuarioEntity(
    Code = Code,
    Comment = Comment,
    DefaultCC1 = DefaultCC1,
    DefaultCC2 = DefaultCC2,
    DefaultCC3 = DefaultCC3,
    DefaultCC4 = DefaultCC4,
    DefaultCC5 = DefaultCC5,
    DefaultCurrency = DefaultCurrency,
    DefaultEmpId = DefaultEmpId,
    DefaultOrderSeries = DefaultOrderSeries,
    DefaultPagoRSeries = DefaultPagoRSeries,
    DefaultPriceList = DefaultPriceList,
    DefaultProyecto = DefaultProyecto,
    DefaultAcctCodeEf = DefaultAcctCodeEf,
    DefaultAcctCodeTr = DefaultAcctCodeTr,
    DefaultAcctCodeCh = DefaultAcctCodeCh,
    DefaultAcctCodeDe = DefaultAcctCodeDe,
    DefaultSNSerieCli = DefaultSNSerieCli,
    DefaultSlpCode = DefaultSlpCode,
    DefaultTaxCode = DefaultTaxCode,
    DefaultWarehouse = DefaultWarehouse,
    DefaultConductor = DefaultConductor,
    DefaultZona = DefaultZona,
    Email = Email,
    IdPhone1 = IdPhone1,
    IdPhone1Val = IdPhone1Val,
    Image = Image,
    Name = Name,
    Password = Password,
    Phone1 = Phone1,
    ShowImage = ShowImage,
    SuperUser = SuperUser,
    CanApprove = CanApprove,
    CanCreate = CanCreate,
    CanDecline = CanDecline,
    CanEditPrice = CanEditPrice,
    CanUpdate = CanUpdate

)


