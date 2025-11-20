package com.mobile.massiveapp.data.model

import com.mobile.massiveapp.data.database.entities.toDatabase
import com.mobile.massiveapp.data.util.MappingInteface
import com.mobile.massiveapp.domain.model.DoClientePago
import com.google.gson.annotations.SerializedName

data class ClientePagos(
    val AccAction: String,
    val AccCreateDate: String,
    val AccCreateHour: String,
    val AccCreateUser: String,
    val AccDocEntry: String,
    val AccNotificado: String,
    val AccError: String,
    val AccFinalized: String,
    val AccMigrated: String,
    val AccMovil: String,
    val AccUpdateDate: String,
    val AccUpdateHour: String,
    val AccUpdateUser: String,
    val Authorized: String,
    val Canceled: String,
    val CardCode: String,
    val CheckAct: String,        // varchar(30)
    val CountryCod: String,      // varchar(3)
    val BankCode: String,        // varchar(30)
    val DueDate: String,         // char(10)
    val CheckSum: Double,         // numeric(19,2) as Float
    val CheckNum: Int,          //
    val CardName: String,
    val CashAcct: String,
    val CashSum: Double,
    val CashSumFC: Double,
    val Comments: String,
    val CounterRef: String,
    val DocCurr: String,
    val DocDate: String,
    val DocDueDate: String,
    val DocEntry: Int,
    val DocNum: Int,
    val DocRate: Double,
    val JrnlMemo: String,
    val PrjCode: String,
    val Series: Int,
    val TaxDate: String,
    val TrsfrAcct: String,
    val TypePayment: String,
    val TrsfrDate: String,
    val TransId: Int,
    val TrsfrRef: String,
    val TrsfrSum: Double,
    val TrsfrSumFC: Double,
    val ObjType: Int,
    @SerializedName("Lineas")
    val clientePagosDetalles: List<ClientePagosDetalle>
):MappingInteface<ClientePagos>(){
    constructor() : this(
        AccAction = "",
        AccCreateDate = "",
        AccCreateHour = "",
        AccCreateUser = "",
        AccDocEntry = "",
        AccError = "",
        AccNotificado = "",
        AccFinalized = "",
        AccMigrated = "",
        AccMovil = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        Authorized = "",
        Canceled = "",
        CardCode = "",
        CheckAct = "",
        CountryCod = "",
        BankCode = "",
        DueDate = "",
        CheckSum = 0.0,
        CheckNum = 0,
        CardName = "",
        CashAcct = "",
        CashSum = 0.0,
        CashSumFC = 0.0,
        Comments = "",
        CounterRef = "",
        DocCurr = "",
        DocDate = "",
        DocDueDate = "",
        DocEntry = 0,
        DocNum = 0,
        DocRate = 0.0,
        JrnlMemo = "",
        PrjCode = "",
        Series = 0,
        TaxDate = "",
        TrsfrAcct = "",
        TypePayment = "",
        TrsfrDate = "",
        TransId = -1,
        TrsfrRef = "",
        TrsfrSum = 0.0,
        TrsfrSumFC = 0.0,
        ObjType = -1,
        clientePagosDetalles = emptyList()
    )

    override fun map(data: List<ClientePagos>): List<*> {
        return data.map { it.toDatabase() }
    }

    override fun listOfKeys(data: List<ClientePagos>): List<Any> {
        return data.map { it.AccDocEntry }
    }
}

fun DoClientePago.toModel() = ClientePagos(
    AccAction = AccAction,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccDocEntry = AccDocEntry,
    AccError = AccError,
    AccNotificado = AccNotificado,
    AccFinalized = AccFinalized,
    AccMigrated = AccMigrated,
    AccMovil = AccMovil,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
    Authorized = Authorized,
    Canceled = Canceled,
    CardCode = CardCode,
    CardName = CardName,
    CashAcct = CashAcct,
    CheckAct = CheckAct,        // varchar(30)
    CountryCod = CountryCod,      // varchar(3)
    BankCode = BankCode,        // varchar(30)
    DueDate = DueDate,         // char(10)
    CheckSum = CheckSum,         // numeric(19,2) as Float
    CheckNum = CheckNum,          //
    CashSum = CashSum,
    CashSumFC = CashSumFC,
    Comments = Comments,
    CounterRef = CounterRef,
    DocCurr = DocCurr,
    DocDate = DocDate,
    DocDueDate = DocDueDate,
    DocEntry = DocEntry,
    DocNum = DocNum,
    DocRate = DocRate,
    JrnlMemo = JrnlMemo,
    PrjCode = PrjCode,
    Series = Series,
    TaxDate = TaxDate,
    TrsfrAcct = TrsfrAcct,
    TypePayment = TypePayment,
    TrsfrDate = TrsfrDate,
    TransId = TransId,
    TrsfrRef = TrsfrRef,
    TrsfrSum = TrsfrSum,
    TrsfrSumFC = TrsfrSumFC,
    ObjType = ObjType,
    clientePagosDetalles = emptyList()
)