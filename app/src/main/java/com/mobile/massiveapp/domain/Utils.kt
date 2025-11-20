package com.mobile.massiveapp.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.mobile.massiveapp.data.database.entities.ErrorLogEntity
import com.mobile.massiveapp.domain.model.DoConfiguracion
import com.mobile.massiveapp.domain.model.DoUsuario
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.getHoraActual

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

fun obtenerUnidadesDeMedida(input: String): List<String> {
    val productos = mutableListOf<String>()

    try {
        val parts = input.split("-")
        if (parts.size == 2) {
            val producto1 = parts[1].substringAfter(":").trim()
            val producto2 = parts[2].substringAfter(":").trim()

            productos.add(producto1)
            productos.add(producto2)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return productos
}

fun obtenerUnidadMedidaMayor(input: String): String =
    try {
        val parts = input.split("-")
        val producto1 = parts[0].substringAfter(":").trim()
        producto1
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

fun getError(code:String, message: String):ErrorLogEntity{
    return ErrorLogEntity(
        ErrorHour = getHoraActual(),
        ErrorDate = getFechaActual(),
        ErrorMessage = message,
        ErrorCode = code
    )
}

fun getRadmonNumber(): Int {
    return (1..16).random()
}



fun getUrlFromConfiguracion(configuracion: DoConfiguracion) =
    if (configuracion.SetIpPublica == true){
        "http://${configuracion.IpPublica}:${configuracion.NumeroPuerto}/WebS_APPMOVIL.asmx"
    } else {
        "http://${configuracion.IpLocal}:${configuracion.NumeroPuerto}/WebS_APPMOVIL.asmx"
    }

fun getErrorId(usuario: DoUsuario) =
    "${usuario.Code}"


fun getUrlFromPuertoEIpPublica(puerto: String, ipPublica: String) =
    "http://$ipPublica:$puerto/WebS_APPMOVIL.asmx"