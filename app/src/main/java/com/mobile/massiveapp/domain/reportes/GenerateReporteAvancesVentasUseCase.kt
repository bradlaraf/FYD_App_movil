package com.mobile.massiveapp.domain.reportes

import android.content.Context
import com.mobile.massiveapp.data.database.dao.SociedadDao
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.GeneralRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.ReportesRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.ui.view.reportes.pdf.PdfReporteAvanceDeVentas
import javax.inject.Inject

class GenerateReporteAvancesVentasUseCase @Inject constructor(
    private val reportesRepository: ReportesRepository,
    private val generalRepository: GeneralRepository,
    private val loginRepository: LoginRepository,
    private val sociedadDao: SociedadDao,
    private val configuracionRepository: ConfiguracionRepository
) {
    suspend operator fun invoke(fechaInicio: String, fechaFin: String, context: Context): Boolean =
        try {
            val usuario = loginRepository.getUsuarioFromDatabase()
            val configuracion = configuracionRepository.getConfiguracion()
            val url = getUrlFromConfiguracion(configuracion)

            val nombreSociedad = sociedadDao.getSociedadDefault().CompnyName
            val nombreVendedor = generalRepository.getVendedorDefault().SlpName

            /*val titulos = reportesRepository.getReporteAvancesDeVentasCabecera(fechaInicio, fechaFin)

            val reporteAvancesVentasList = titulos.map {

                val listaArticulos = reportesRepository.getReporteAvancesDeVentas(it.FirmCode)
                listOf(it.toDoReporteAvanceVentas()) + listaArticulos
            }*/

            val listaReporte =
                reportesRepository.getReporteAvanceVentasFromWS(
                    fechaInicio = fechaInicio,
                    fechaFin =  fechaFin,
                    usuario = usuario,
                    configuracion = configuracion,
                    url = url,
                    timeout = 30L
                )
            val listaAgrupada = listaReporte.groupBy { it.FirmName }

            val pdf = PdfReporteAvanceDeVentas(
                nombreVendedor,
                nombreSociedad,
                listaAgrupada,
                context)

            pdf()
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}