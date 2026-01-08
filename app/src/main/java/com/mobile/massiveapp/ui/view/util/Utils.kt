package com.mobile.massiveapp.ui.view.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mobile.massiveapp.data.model.ClientePagos
import com.mobile.massiveapp.data.model.ClientePagosDetalle
import com.mobile.massiveapp.data.model.ClientePedidoDetalle
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.data.model.ConfigurarUsuarios
import com.mobile.massiveapp.data.model.ConsultaDocumentoDireccion
import com.mobile.massiveapp.domain.model.DoClientePagoDetalle
import com.mobile.massiveapp.domain.model.DoClienteSocios
import com.mobile.massiveapp.domain.model.DoSocioContactos
import com.mobile.massiveapp.domain.model.DoSocioDirecciones
import com.google.android.material.datepicker.MaterialDatePicker
import com.mobile.massiveapp.data.model.LiquidacionPago
import com.mobile.massiveapp.domain.model.DoLiquidacionPago
import java.io.File
import java.text.DecimalFormat
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

//Dar formato a los Double
/*fun Double.format(digits: Int) = "%.${digits}f".format(this).toDouble()*/

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer(t)
            removeObserver(this) // Se elimina automáticamente después de recibir un cambio
        }
    })
}

fun String.formatoSinModena(moneda: String):Double {
    return this.replace(moneda, "").trim().toDouble()
}

fun clearInternalCache(context: Context) {
    val cacheDir = context.cacheDir
    deleteDir(cacheDir)
}

private fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory) {
        val children = dir.list()
        children?.forEach {
            val success = deleteDir(File(dir, it))
            if (!success) {
                return false
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile) {
        dir.delete()
    } else {
        false
    }
}

fun clearExternalCache(context: Context) {
    val externalCacheDir = context.externalCacheDir
    deleteDir(externalCacheDir)
}



fun String.parseLocalizedDouble(): Double {
    val cleanedString = this.replace(",", ".")
    return cleanedString.toDouble()
}

fun Double.format(digits: Int): Double {
    val formattedString = "%.${digits}f".format(this)
    return formattedString.parseLocalizedDouble()
}

fun String.getBoolByYorN(): Boolean{
    return this == "Y"
}

fun String.getBoolFromAccLocked(): Boolean{
    return this == "N"
}

fun Boolean.getStringBool(): String{
    val result = if (this){
        "Y"
    } else {
        "N"
    }
    return result
}


fun Boolean.getStringForAccLocked(): String{
    val result = if (!this){
        "Y"
    } else {
        "N"
    }
    return result
}
fun Double.formatString(digits: Int): String {
    return "%.${digits}f".format(this)
}

fun Double.formato(digits: Int): Double {
    var formato = DecimalFormat()
    for (i in 0 until digits){
        formato = DecimalFormat("#.$i")
    }
    return formato.format(this).toDouble()
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatearFecha(cadenaFecha: String): String {
    val formatoEntrada = DateTimeFormatter.ofPattern("yyyyMMdd")
    val formatoSalida = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val fecha = LocalDate.parse(cadenaFecha, formatoEntrada)
    return fecha.format(formatoSalida)
}

fun setCopyToClipboardOnLongClick(
    constraintLayout: ConstraintLayout,
    textView: TextView,
    context: Context
) {
    constraintLayout.setOnLongClickListener {
        val textoACopiar = textView.text.toString()
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Texto Copiado", textoACopiar)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Texto copiado", Toast.LENGTH_SHORT).show()
        true
    }
}


//Para limitar el tamaño de un String
fun String.limitTo(maxLength: Int): String {
    return if (length <= maxLength) {
        this
    } else {
        substring(0, maxLength) + "..."
    }
}

//Para poder usar setOnClickListeners en un List de Views
fun <T : View> List<T>.setOnClickListeners(onClickListener: (T) -> Unit) {
    forEach { view ->
        view.setOnClickListener {
            onClickListener(it as T)
        }
    }
}

/*fun AppCompatActivity.hideKeyboard() {
    val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = currentFocus
    currentFocusView?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}*/

fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun <K, V> Map<K, V?>.allValuesNotNullOrEmpty(): Boolean {
    return values.all { it != null && it.toString().isNotEmpty() }
}

fun obtenerCondicionPagoPorCodigo(codigo:Int): String {
    when(codigo){
        -1 -> return "Contado"
        1 -> return "Letra a 45 días"
        2 -> return "Letra a 60 días"
        3 -> return "Letra a 90 días"
        4 -> return "Crédito a 120 días"
        5 -> return "Letra a 120 días"
        6 -> return "Crédito a 30 días"
        7 -> return "Crédito a 60 días"
        8 -> return "Crédito a 90 días"
        9 -> return "Letra a 15 días"
        10 -> return "Letra a 30 días"
        11 -> return "Crédito a 45 días"
        12 -> return "Crédito a 15 días"
        else -> {
            return "No tiene un Codigo de Condicion de Pago válido"}
    }
}

fun obtenerTipoDocumentoIndicadorPorCodigo(codigo:String):String{
    return when(codigo){
        "01" -> "01-Factura"
        "03" -> "03-Boleta"

        else -> {
            "No tiene un codigo de documento válido"
        }
    }
}

fun obtenerFormatoMonedas(currency:String):String{
    return when(currency){
        "## "-> "TODAS"
        "SOL"-> "SOLES"
        "USD"-> "DOLARES"

        else -> "No tiene un codigo de moneda válido"
    }
}

fun getHoraActual(): String{
    val calendar = Calendar.getInstance()
    val hourFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val formatteddate = hourFormat.format(calendar.time)
    return formatteddate
}

fun getFechaActual():String{
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatteddate = dateFormat.format(calendar.time)
    return formatteddate
}

fun getFechaAyer(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)  // Restar un día
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun showMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun normalizeText(text: String): String {
    val normalizado = Normalizer.normalize(text, Normalizer.Form.NFKD)
    return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalizado).replaceAll("")
}

fun mostrarCalendario(context: Context, onAcceptDateListener:(String, String, String)->Unit) {

    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Crear un DatePickerDialog
    val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->
        onAcceptDateListener(selectedDay.toString(), (selectedMonth + 1).toString(),selectedYear.toString() )
    }, year, month, day)
    datePickerDialog.actionBar?.hide()
    datePickerDialog.window?.windowStyle

    // Mostrar el diálogo
    datePickerDialog.show()

}



fun folioFormat(input: String): String {
    val partes = input.split("-")

    return if (partes.size == 2 && partes[0].length == 4) {
        val numero = partes[1]
        val numeroFormateado = numero.padStart(8, '0')

        "${partes[0]}-$numeroFormateado"
    } else {
        // El formato de entrada no es el esperado, devolver el mismo String.
        input
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun mostrarCalendarioMaterial(context: Context, fechaActual: String, onAcceptDateListener:(dia:String, mes:String, year:String)->Unit){
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    val initialDate = dateFormat.parse(fechaActual)
    if (initialDate != null){

        calendar.time = initialDate

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccione una fecha")
            .setSelection(calendar.timeInMillis)
            .build()

        datePicker.show((context as AppCompatActivity).supportFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = Date(it)
            val oCalendar = Calendar.getInstance()
            oCalendar.time = selectedDate

            val utcDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
            // create an object converting from UTC to the device´s timezone
            val myTimeZoneDate = ZonedDateTime.of(utcDate, ZoneId.systemDefault())
            // get the long timestamp to use as you want
            val myTimeZoneDateLong = myTimeZoneDate.toInstant().toEpochMilli()

            val myTimeZoneDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(myTimeZoneDateLong), ZoneId.systemDefault())

            val year = myTimeZoneDateTime.year.toString()
            val month = String.format("%02d", myTimeZoneDateTime.monthValue)
            val day = String.format("%02d", myTimeZoneDateTime.dayOfMonth)
            onAcceptDateListener(day, month, year)
        }
    }
}

fun getCodigoDeDocumentoActual(context: Context):String{
    val calendar = Calendar.getInstance()
    val dateFormatDoc = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    val formatteddateDoc = dateFormatDoc.format(calendar.time)

    //Setea el numero de documento actual
    val androidID = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    val codigoDoc = "$androidID-$formatteddateDoc-1"
    return codigoDoc
}



fun agregarSocioNegocio(
    cardCode: String,
    cardName: String,
    numeroDocumento: String,
    tipoDocumento: String,
    slpCode: Int,
    indicador: String,
    activo: String,
    habido: String,
    accDocEntry: String,
    groupCode: Int,
    currency: String,
    groupNum: Int,
    listNum: Int,
    serieSocio: Int,
    telefono1: String,
    telefono2: String,
    telefonoMovil: String,
    usuario: String,
    primerNombre: String,
    segundoNombre: String,
    apellidoPaterno: String,
    apellidoMaterno: String,
    tipoPersona: String,
    agentePercepcion: String,
    agenteRetenedor: String,
    buenContribuyente: String,
    personaNaturalConNegocio: String,
    consultado: String,
    zone: String,
    correo: String,
    cardFName: String = "",
    fechaConsulta: String

) = DoClienteSocios(
    CardCode = cardCode,
    CardName = cardName.replace("\n"," "),
    LicTradNum = numeroDocumento,
    U_MSV_LO_TIPOPER = tipoPersona,
    U_MSV_LO_TIPODOC = tipoDocumento,
    U_MSV_LO_PRIMNOM = primerNombre,
    U_MSV_LO_SEGUNOM = segundoNombre,
    U_MSV_LO_APELPAT = apellidoPaterno,
    U_MSV_LO_APELMAT = apellidoMaterno,
    CardType = "L",
    GroupCode = groupCode,
    GroupNum = groupNum,
    SlpCode = slpCode,
    Indicator = indicador,
    CreditLine = 0.0,
    Zone = zone,
    Currency = currency,
    ListNum = listNum,
    Series = serieSocio,
    ShipToDef = "DESPACHO",
    BillToDef = "FISCAL",
    AccAction = "I",
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = usuario,
    AccDocEntry = accDocEntry,
    AccError = "N",
    AccFinalized = "N",
    AccLocked = "N",
    AccControl = "N",
    AccMigrated = "N",
    AccMovil = "Y",
    AccUpdateDate = "",
    E_Mail = correo,
    RGuia = "N",
    AccUpdateHour = "",
    AccUpdateUser = "",
    Cellular = telefonoMovil,
    Phone1 = telefono1,
    Phone2 = telefono2,
    CANCELED = "N",
    DocEntry = -1
)


fun actualizarPagoCabecera(
    accDocEntry: String,
    cardCode: String,
    cardName: String,
    comentarios: String,
    glosa: String,
    moneda: String,
    typePayment: String,
    usuario: String,
    numeroDeSeriePago: Int,
    accAction: String,
    horaCreacion: String,
    fechaCreacion: String,
    horaEdicion: String,
    fechaEdicion: String,
    taxDate: String,
    docNum: Int,
    transId: Int,
    accNotificado: String,
    docEntry: Int,

    //Efectivo---------
    montoEfectivo: Double,
    cuentaCash:String,

    //Transferencia--------
    fechaTranferencia:String,
    numeroReferenciaTransferencia:String,
    montoTotalTransferencia: Double,
    transferenciaCuenta: String,

    //Cheque-------
    CountryCod: String = "PE",
    CheckAct: String = "",
    BankCode: String = "",
    DueDate: String,
    CheckSum: Double,
    CheckNum: Int,

    listaDetalles: List<ClientePagosDetalle>
) =
    ClientePagos(
        AccAction = accAction,
        AccCreateDate = fechaCreacion,
        AccCreateHour = horaCreacion,
        AccCreateUser = usuario,
        AccDocEntry = accDocEntry,
        AccError = "",
        AccNotificado = accNotificado,
        AccFinalized = "N",
        AccMigrated = "N",
        AccMovil = "Y",
        AccUpdateDate = fechaEdicion,
        AccUpdateHour = horaEdicion,
        AccUpdateUser = usuario,
        Authorized = "A",
        Canceled = "N",
        CardCode = cardCode,
        CardName = cardName,
        CashAcct = cuentaCash,
        CashSum = montoEfectivo,
        CashSumFC = 0.0,
        CheckAct = CheckAct,            // varchar(30)
        CountryCod = CountryCod,      // varchar(3)
        BankCode = BankCode,        // varchar(30)
        DueDate = DueDate,         // char(10)
        CheckSum = CheckSum,         // numeric(19,2) as Float
        CheckNum = CheckNum,
        Comments = comentarios,
        CounterRef = "",
        DocCurr = moneda,
        DocDate = getFechaActual(),
        DocDueDate = getFechaActual(),
        DocEntry = docEntry,
        DocNum = docNum,
        DocRate = 1.0,
        JrnlMemo = glosa,
        PrjCode = "",
        Series = numeroDeSeriePago,     //DefaultSeriesRecibido del Usuario
        TaxDate = taxDate,
        TrsfrAcct = transferenciaCuenta,
        TrsfrDate = fechaTranferencia,
        TrsfrRef = numeroReferenciaTransferencia,
        TrsfrSum = montoTotalTransferencia,
        TypePayment = typePayment,
        TransId = transId,
        TrsfrSumFC = 0.0,
        ObjType = 140,
        AccControl = "N",
        clientePagosDetalles = listaDetalles
    )


fun agregarPagoCabecera(
    accDocEntry: String,
    cardCode: String,
    cardName: String,
    comentarios: String,
    moneda: String,
    typePayment: String,
    usuario: String,
    numeroDeSeriePago: Int,
    glosa: String,
    slpCode: Int,

    //Efectivo-------
    cuentaCash:String,
    montoTotal: Double,


    //Transferencia-------
    transferenciaCuenta: String,
    montoTotalTransferencia: Double,
    numeroReferenciaTransferencia:String,
    fechaTranferencia:String,

    //Cheque-----
    CheckAct: String = "",
    CountryCod: String = "PE",
    BankCode: String = "",
    DueDate: String = "",
    CheckSum: Double = 0.0,
    CheckNum: Int = 0,

    listaDetalles: List<ClientePagosDetalle>
) =
    ClientePagos(
        AccAction = "I",
        AccCreateDate = getFechaActual(),
        AccCreateHour = getHoraActual(),
        AccCreateUser = usuario,
        AccNotificado = "N",
        AccDocEntry = accDocEntry,
        AccError = "",
        AccFinalized = "N",
        AccMigrated = "N",
        AccMovil = "Y",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        Authorized = "A",
        Canceled = "N",
        CardCode = cardCode,
        CardName = cardName,
        CashAcct = cuentaCash,
        CashSum = montoTotal,
        CashSumFC = 0.0,
        Comments = comentarios,
        CheckAct = CheckAct,            // varchar(30)
        CountryCod = CountryCod,        // varchar(3)
        BankCode = BankCode,            // varchar(30)
        DueDate = DueDate,              // char(10)
        CheckSum = CheckSum,            // numeric(19,2) as Float
        CheckNum = CheckNum,            //
        CounterRef = slpCode.toString(),
        DocCurr = moneda,
        DocDate = getFechaActual(),
        DocDueDate = getFechaActual(),
        DocEntry = -1,
        DocNum = -1,
        DocRate = 1.0,
        JrnlMemo = glosa,
        PrjCode = "",
        Series = numeroDeSeriePago,
        TaxDate = getFechaActual(),
        TrsfrAcct = transferenciaCuenta,
        TrsfrDate = fechaTranferencia,
        TransId = -1,
        TrsfrRef = numeroReferenciaTransferencia,
        TrsfrSum = montoTotalTransferencia,
        TypePayment = typePayment,
        TrsfrSumFC = 0.0,
        clientePagosDetalles = listaDetalles,
        ObjType = 140,
        AccControl = "N"
    )


fun agregarPagoDetalle(
    monto:Double,
    accDocEntry: String,
    docLine: Int,
    usuario: String,
    instId: Int,
    docEntryFactura: Int
)
    = DoClientePagoDetalle(
        AccAction = "I",
        AccCreateDate = getFechaActual(),
        AccCreateHour = getHoraActual(),
        AccCreateUser = usuario,
        AccDocEntry = accDocEntry,
        AccMigrated = "N",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        AppliedFC = 0.0,
        DocEntry = docEntryFactura,
        DocLine = docLine,
        DocNum = -1,
        DocTransId = -1,
        InstId = instId,
        InvType = 13,
        SumApplied = monto,
        ObjType = 140,
        AccControl = "N"
    )

fun agregarPagoLiquidacion(
    docLine: Int,
    monto:Double,
    accDocEntry: String,
    numeroOperacion: String,
    numeroCuenta: String,
    moneda: String,
    medio: String,
    instId: Int,
    liquidacion: Int,
    manifiesto: Int,
    docEntryFactura: Int
) = DoLiquidacionPago(
    AccDocEntry = accDocEntry,
    Code = "",
    Name = "",
    DocEntry = -1,
    U_MSV_MA_LIQ = liquidacion,
    U_MSV_MA_MANIF = manifiesto,
    U_MSV_MA_OBJETO = -1,
    U_MSV_MA_CLAVE = docEntryFactura,
    U_MSV_MA_FECHA = getFechaActual(),
    U_MSV_MA_MEDIO = medio,
    U_MSV_MA_MON = moneda,
    U_MSV_MA_IMP = monto,
    U_MSV_MA_NROOPE = numeroOperacion,
    U_MSV_MA_CTA = numeroCuenta,
    U_MSV_MA_PAGO = instId,
    EditableMovil = "Y",
    DocLine = docLine
)

fun actualizarPagoDetalle(
    monto:Double,
    accDocEntry: String,
    docLine: Int,
    usuario: String,
    instId: Int,
    docEntryFactura: Int,
    accAction: String,
    fechaCreacion: String,
    horaCreacion: String,
    fechaEdicion: String,
    horaEdicion: String,
    docNum: Int
)
        = DoClientePagoDetalle(
    AccAction = accAction,
    AccCreateDate = fechaCreacion,
    AccCreateHour = horaCreacion,
    AccCreateUser = usuario,
    AccDocEntry = accDocEntry,
    AccMigrated = "N",
    AccUpdateDate = fechaEdicion,
    AccUpdateHour = horaEdicion,
    AccUpdateUser = usuario,
    AppliedFC = 0.0,
    DocEntry = docEntryFactura,
    DocLine = docLine,
    DocNum = docNum,
    DocTransId = -1,
    InstId = instId,
    InvType = 13,
    SumApplied = monto,
    ObjType = 140,
    AccControl = "N"
)

fun agregarDireccion(
    lineNum: Int,
    ubigeo: String,
    codigoPais: String,
    calle: String,
    codigoDepartamento: String,
    provincia: String,
    distrito: String,
    cardCode: String,
    tipo: String,
    accDocEntry: String,
    usuario: String,
    zona: String,
    latitud: String,
    longitud: String,
    vendedor: Int

) = DoSocioDirecciones(
    AccAction = "I",
    AccCreateDate = getFechaActual(),
    AccCreateHour = getHoraActual(),
    AccCreateUser = usuario,
    AccDocEntry = accDocEntry,
    AccLocked = "N",
    AccMigrated = "N",
    AccUpdateDate = "",
    AccUpdateHour = "",
    AccUpdateUser = "",
    AccControl = "N",
    Address = if(tipo == "B") "FISCAL" else if(tipo == "S") "DESPACHO" else "",
    AdresType = tipo,
    Block = "",
    CardCode = cardCode,
    City = distrito,
    Country = codigoPais,
    County = provincia,
    LineNum = lineNum,
    State = codigoDepartamento,
    Street = calle,
    U_MSV_CP_LATITUD = latitud,
    U_MSV_CP_LONGITUD = longitud,
    U_MSV_MA_VEN = vendedor,
    U_MSV_MA_ZONA = zona,
    U_MSV_FE_UBI = ubigeo,
    ZipCode = distrito
)


fun agregarDireccionConsultaDocumento(
    ubigeo: String,
    codigoPais: String,
    calle: String,
    codigoDepartamento: String,
    provincia: String,
    distrito: String,
    cardCode: String,
    tipo: String,

) = ConsultaDocumentoDireccion(
    Calle = calle,
    Codigo = cardCode,
    CodigoPostal = ubigeo,
    DepartamentoCodigo = codigoDepartamento,
    DepartamentoNombre = "",
    DistritoCodigo = distrito,
    DistritoNombre = "",
    DistritoNombreAlterno = "",
    Estacion = "",
    Fecha = "",
    Latitud = 0.0,
    Longitud = 0.0,
    Nombre = "",
    NumeroDocumento = "",
    PaisCodigo = codigoPais,
    PaisNombre = "",
    ProvinciaCodigo = provincia,
    ProvinciaNombre = "",
    Referencia = "",
    Tipo = tipo
)




fun agregarSocioContacto(
    accDocEntry: String,
    cardCode: String,
    nombre: String,
    celular: String,
    telefono1: String,
    email: String,
    usuario: String,
    licTradNum: String
)= DoSocioContactos(
        AccAction = "I",
        AccCreateDate = getFechaActual(),
        AccCreateHour = getHoraActual(),
        AccCreateUser = usuario,
        AccDocEntry = accDocEntry,
        AccLocked = "N",
        AccControl = "N",
        AccMigrated = "N",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccUpdateUser = "",
        CardCode = cardCode,
        Cellolar = celular,
        CntctCode = getHoraActual().takeLast(2).toInt(),
        E_Mail = email,
        LicTradNum = licTradNum,
        Name = nombre,
        Position = "",
        Tel1 = telefono1,
        Tel2 = ""
    )

fun actualizarCabeceraDePedido(
    accDocEntry: String,
    accNotificado: String,
    cardCode:String,
    cardName: String,
    licTradNum: String,
    cntctCode: Int,
    docDate: String,
    taxDate: String,
    docDueDate:String,
    priceList: Int,
    groupNum:Int,
    indicator:String,
    slpCode:Int,
    payToCode:String,
    direccionFiscal:String,
    shipToCode:String,
    direccionDespacho:String,
    valorVenta:Double,
    impuestoTotal:Double,
    total:Double,
    docCur: String,
    comentario:String,
    codigoUsuario:String,
    fechaCreacion:String,
    horaCreacion:String,
    numeroSeriePedido: Int,
    fechaActualizacion: String,
    horaActualizacion: String,
    docEntry: Int,
    docNum: Int,
    accDocEntrySN: String,
    detallePedido: List<ClientePedidoDetalle>

):ClientePedidos{
    val pedidoCabecera = ClientePedidos(
        AccDocEntrySN = accDocEntrySN,
        AccDocEntry = accDocEntry,
        AccNotificado = accNotificado,
        Series = numeroSeriePedido,
        DocEntry = docEntry,
        DocNum = docNum ,
        Authorized = "A",
        DocStatus = "O",
        CANCELED = "N",
        CardCode = cardCode,
        CardName = cardName,
        LicTradNum = licTradNum,
        CntctCode = cntctCode,
        NumAtCard = "",         //Debe ser un parametro en la aplicacion - por ahora en blanco
        DocDate = docDate,
        TaxDate = taxDate,
        DocDueDate = docDueDate,
        DocCur = docCur,
        DocRate = 1.0,
        PriceList = priceList,
        GroupNum = groupNum,                //Código de condiciones de pago
        Indicator = indicator,
        SlpCode = slpCode,                  //Código del empleado del departamento de ventas
        PayToCode = payToCode,
        Address = direccionFiscal,
        ShipToCode = shipToCode,
        Address2 = direccionDespacho,
        GrosProfit = valorVenta,
        GrosProfFC = 0.0,
        VatSum = impuestoTotal,
        VatSumFC = 0.0,
        DocTotal = total,
        DocTotalFC = 0.0,
        Project = "",
        Comments = comentario,
        AccCreateUser = codigoUsuario,
        AccCreateDate = fechaCreacion,
        AccCreateHour = horaCreacion,
        AccUpdateUser = codigoUsuario,
        AccUpdateDate = fechaActualizacion,
        AccUpdateHour = horaActualizacion,
        AccMovil = "Y",
        AccMigrated = "N",
        AccAction = "U",
        AccFinalized = "N",
        AccError = "",
        ObjType = 112,
        AccControl = "N",
        clientePedidoDetalles = detallePedido
    )

    return pedidoCabecera

}

fun actualizarDetalleDePedido(
    usuario: String,
    codigo:String,
    nombre:String,
    cantidad:Double,
    grupoUM:String,
    unidadMedida:String,
    precio:Double,
    precioBruto: Double,
    porcentajeDescuento: Double,
    total:Double,
    lineNum:Int,
    accDocEntry:String,
    fechaActual: String,
    horaActual: String,
    impuesto:Double,
    codigoImpuesto: String,
    listaPrecios: Int,
    codigoAlmacen: String,
    fechaActualizacion: String,
    horaActualizacion: String,
    migrado: String,
    accAction: String,
    uomEntry: Int,
    docEntry: Int

): ClientePedidoDetalle {
    val pedidoArticulo = ClientePedidoDetalle(
        AccAction = accAction,
        AccCreateDate = fechaActual,
        AccCreateHour = horaActual,
        AccCreateUser = usuario,
        AccDocEntry = accDocEntry,
        AccMigrated = migrado,
        AccUpdateDate = fechaActualizacion,                 //Cuando se actualiza
        AccUpdateHour = horaActualizacion,
        AccUpdateUser = usuario,
        DiscPrcnt = porcentajeDescuento,    //Descuento
        DocEntry = docEntry,
        Dscription = nombre,                //ItemName
        GTotal = total + impuesto,          //Transferencia gratuita
        GTotalFC = 0.0,
        ItemCode = codigo,
        LineNum = lineNum,
        LineTotal = precio*cantidad.toDouble(),  //Total de la linea
        OcrCode = "",
        OcrCode2 = "",
        OcrCode3 = "",
        OcrCode4 = "",
        OcrCode5 = "",
        Price = precio,
        PriceAfVAT = precio,                //Precio con IGV descontado aplicado al impuesto
        PriceBefDi = precioBruto,           //Precio sin IGV
        PriceList = listaPrecios,           //Lista de precio
        Project = "",
        Quantity = cantidad,
        TaxCode = codigoImpuesto,           //Codigo de impuesto
        TotalFrgn = 0.0,                  //Total en moneda extranjera
        UnitMsr = unidadMedida,
        UomCode = grupoUM,                  //Grupo unidad de medida
        UomEntry = uomEntry,
        VatSum = impuesto,                  //Suma total igv
        VatSumFrgn = 0.0,                   //Suma total igv en moneda extranjera
        WhsCode = codigoAlmacen,            //Almacen
        ObjType = 112,
        AccControl = "N",

    )
    return pedidoArticulo
}

fun agregarDetalleDePedido(
    usuario: String,
    codigo:String,
    nombre:String,
    cantidad:Double,
    grupoUM:String,
    unidadMedida:String,
    precio:Double,
    precioAftVat: Double = 0.0,
    precioLP: Double = 0.0,
    precioBruto: Double,
    porcentajeDescuento: Double,
    total:Double,
    lineNum:Int,
    accDocEntry:String,
    fechaActual: String,
    horaActual: String,
    impuesto:Double,
    codigoImpuesto: String,
    listaPrecios: Int,
    codigoAlmacen: String,
    uomEntry: Int

): ClientePedidoDetalle {
    val gtotals = (precioAftVat*cantidad).format(2)
    val linetotals = (precio*cantidad).format(2)
    val pedidoArticulo = ClientePedidoDetalle(
        AccAction = "I",
        AccCreateDate = fechaActual,
        AccCreateHour = horaActual,
        AccCreateUser = usuario,
        AccDocEntry = accDocEntry,
        AccMigrated = "N",
        AccUpdateDate = "",                 //Cuando se actualiza
        AccUpdateHour = "",
        AccUpdateUser = "",
        DiscPrcnt = porcentajeDescuento,    //Descuento
        DocEntry = -1,                      //Va vacio
        Dscription = nombre,                //ItemName
        GTotal = (precioAftVat*cantidad).format(2),          //Transferencia gratuita
        GTotalFC = 0.0,
        ItemCode = codigo,
        LineNum = lineNum,
        LineTotal = (precio*cantidad).format(2),  //Total de la linea
        OcrCode = "",
        OcrCode2 = "",
        OcrCode3 = "",
        OcrCode4 = "",
        OcrCode5 = "",
        Price = precio,                     //Precio descontado
        PriceAfVAT = precioAftVat,                //Precio con IGV descontado aplicado al impuesto
        PriceBefDi = precioLP,           //Precio sin IGV Precio sin descuento
        PriceList = listaPrecios,           //Lista de precio
        Project = "",
        Quantity = cantidad,
        TaxCode = codigoImpuesto,           //Codigo de impuesto
        TotalFrgn = 0.0,                  //Total en moneda extranjera
        UnitMsr = unidadMedida,
        UomCode = grupoUM,                  //Grupo unidad de medida
        UomEntry = uomEntry,
        VatSum = gtotals-linetotals.format(2),                  //Suma total igv
        VatSumFrgn = 0.0,                   //Suma total igv en moneda extranjera
        WhsCode = codigoAlmacen,            //Almacen
        ObjType = 112,
        AccControl = "N",
    )
    return pedidoArticulo
}

fun agregarCabeceraDePedido(
    accDocEntry: String,
    cardCode:String,
    cardName: String,
    licTradNum: String,
    cntctCode: Int,
    docDate: String,
    taxDate: String,
    docDueDate:String,
    priceList: Int,
    groupNum:Int,
    indicator:String,
    slpCode:Int,
    payToCode:String,
    direccionFiscal:String,
    shipToCode:String,
    direccionDespacho:String,
    valorVenta:Double,
    impuestoTotal:Double,
    total:Double,
    docCur: String,
    comentario:String,
    codigoUsuario:String,
    fechaCreacion:String,
    horaCreacion:String,
    numeroSeriePedido: Int,
    accDocEntrySN: String = "",
    detallePedido: List<ClientePedidoDetalle>

):ClientePedidos{
    val pedidoCabecera = ClientePedidos(
        AccDocEntrySN = accDocEntrySN,
        AccDocEntry = accDocEntry,
        AccNotificado = "N",
        Series = numeroSeriePedido,
        DocEntry = -1,
        DocNum = -1,
        Authorized = "A",
        DocStatus = "O",
        CANCELED = "N",
        CardCode = cardCode,
        CardName = cardName,
        LicTradNum = licTradNum,
        CntctCode = cntctCode,
        NumAtCard = "",         //Debe ser un parametro en la aplicacion - por ahora en blanco
        DocDate = docDate,
        TaxDate = taxDate,
        DocDueDate = docDueDate,
        DocCur = docCur,
        DocRate = 1.0,
        PriceList = priceList,
        GroupNum = groupNum,                //Código de condiciones de pago
        Indicator = indicator,
        SlpCode = slpCode,                  //Código del empleado del departamento de ventas
        PayToCode = payToCode,
        Address = direccionFiscal,
        ShipToCode = shipToCode,
        Address2 = direccionDespacho,
        GrosProfit = valorVenta,
        GrosProfFC = 0.0,
        VatSum = impuestoTotal.format(2),
        VatSumFC = 0.0,
        DocTotal = total.format(2),
        DocTotalFC = 0.0,
        Project = "",
        Comments = comentario,
        AccCreateUser = codigoUsuario,
        AccCreateDate = fechaCreacion,
        AccCreateHour = horaCreacion,
        AccUpdateUser = "",
        AccUpdateDate = "",
        AccUpdateHour = "",
        AccMovil = "Y",
        AccMigrated = "N",
        AccAction = "I",
        AccFinalized = "N",
        AccError = "",
        ObjType = 112,
        AccControl = "N",
        clientePedidoDetalles = detallePedido
    //AccNotificado
        //N por default
    )

    return pedidoCabecera

}

fun solicitarPermisos(context: Context) {
    val permisos = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    AlertDialog.Builder(context)
        .setTitle("Permisos necesarios")
        .setMessage("Para generar el reporte, necesitamos permisos de escritura en el almacenamiento.")
        .setPositiveButton("Solicitar permisos") { _, _ ->
            ActivityCompat.requestPermissions(
                context as Activity,
                permisos,
                1
            )
        }
        .setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
            // Manejar la cancelación aquí si es necesario
        }
        .create()
        .show()
}


fun crearSocioDireccion(
    AccAction: String,
    AccCreateDate: String,
    AccCreateHour: String,
    AccCreateUser: String,
    AccDocEntry: String,
    AccLocked: String,
    AccControl: String,
    AccMigrated: String,
    AccUpdateDate: String,
    AccUpdateHour: String,
    AccUpdateUser: String,
    Address: String,
    AdresType: String,
    Block: String,
    CardCode: String,
    City: String,
    Country: String,
    County: String,
    LineNum: Int,
    State: String,
    Street: String,
    U_MSV_CP_LATITUD: String,
    U_MSV_CP_LONGITUD: String,
    U_MSV_FE_UBI: String,
    zona: String,

    ZipCode: String
) = DoSocioDirecciones (
    AccAction = AccAction,
    AccCreateDate = AccCreateDate,
    AccCreateHour = AccCreateHour,
    AccCreateUser = AccCreateUser,
    AccDocEntry = AccDocEntry,
    AccLocked = AccLocked,
    AccControl = AccControl,
    AccMigrated = AccMigrated,
    AccUpdateDate = AccUpdateDate,
    AccUpdateHour = AccUpdateHour,
    AccUpdateUser = AccUpdateUser,
    Address = Address,
    AdresType = AdresType,
    Block = Block,
    CardCode = CardCode,
    City = City,
    Country = Country,
    County = County,
    LineNum = LineNum,
    State = State,
    Street = Street,
    U_MSV_CP_LATITUD = U_MSV_CP_LATITUD,
    U_MSV_CP_LONGITUD = U_MSV_CP_LONGITUD,
    U_MSV_MA_ZONA = zona,
    U_MSV_MA_VEN = -1,
    U_MSV_FE_UBI = U_MSV_FE_UBI,
    ZipCode = ZipCode,
)


fun alertDialog(
    titulo: String,
    context:Context,
    onAccept: () -> Unit
){
    val builder = AlertDialog.Builder(context)
    builder
        .setTitle(titulo)
        .setPositiveButton("Aceptar"){ _, _ ->
            onAccept()
        }

    val dialog = builder.create()
    dialog.show()
}


fun <T> performActionWithRetry(action: () -> T, maxRetries: Int = 3): T? {
    var currentAttempt = 0
    var result: T? = null

    while (currentAttempt < maxRetries) {
        try {
            currentAttempt++
            result = action()
            return result
        } catch (e: Exception) {
            if (currentAttempt >= maxRetries) {
                throw e
            }
        }
    }

    return result
}

fun deleteDirectory(directory: File) {
    val files = directory.listFiles()
    if (files != null) {
        for (file in files) {
            if (file.isDirectory) {
                deleteDirectory(file)
            } else {
                file.delete()
            }
        }
    }
    directory.delete() // Eliminar el directorio vacío
}

fun validateDir(dir: File) {
    if (!dir.exists()){
        dir.mkdirs()
    }

    val files = dir.listFiles()

    files?.forEach { file ->
        if (file.isFile) {
            file.delete()
        } else if (file.isDirectory) {
            deleteDirectory(file)
        }
    }
}
