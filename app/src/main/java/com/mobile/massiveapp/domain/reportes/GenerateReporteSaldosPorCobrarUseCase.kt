package com.mobile.massiveapp.domain.reportes

import android.content.Context
import com.mobile.massiveapp.data.database.dao.SociedadDao
import com.mobile.massiveapp.data.repositories.ConfiguracionRepository
import com.mobile.massiveapp.data.repositories.GeneralRepository
import com.mobile.massiveapp.data.repositories.LoginRepository
import com.mobile.massiveapp.data.repositories.ReportesRepository
import com.mobile.massiveapp.domain.getUrlFromConfiguracion
import com.mobile.massiveapp.ui.view.reportes.pdf.PdfReporteSaldosPorCobrar
import javax.inject.Inject

class GenerateReporteSaldosPorCobrarUseCase @Inject constructor(
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

            val nombreVendedor = generalRepository.getVendedorDefault().SlpName
            val nombreSociedad = sociedadDao.getSociedadDefault().CompnyName

            val listaReporte = reportesRepository.getReporteSaldosPorCobrarFromWS(
                fechaInicio = fechaInicio,
                fechaFin =  fechaFin,
                usuario = usuario,
                configuracion = configuracion,
                url = url,
                timeout = 30L
            )


            val pdf = PdfReporteSaldosPorCobrar(
                nombreVendedor,
                nombreSociedad,
                listaReporte,
                context
            )
            pdf()
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}