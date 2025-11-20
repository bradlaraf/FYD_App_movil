package com.mobile.massiveapp.domain.reportes

import android.content.Context
import com.mobile.massiveapp.data.database.dao.SociedadDao
import com.mobile.massiveapp.data.repositories.GeneralRepository
import com.mobile.massiveapp.data.repositories.ReportesRepository
import com.mobile.massiveapp.domain.model.toDoReporteProductosPorMarca
import com.mobile.massiveapp.ui.view.reportes.pdf.PdfReporteProductosPorMarca
import javax.inject.Inject

class GenerateReporteProductosPorMarcaUseCase @Inject constructor (
    private val reportesRepository: ReportesRepository,
    private val generalRepository: GeneralRepository,
    private val sociedadDao: SociedadDao
) {
    suspend operator fun invoke(context: Context): Boolean =
        try {
            val nombreVendedor = generalRepository.getVendedorDefault().SlpName
            val nombreSociedad = sociedadDao.getSociedadDefault().CompnyName

            val listaCabecera = reportesRepository.getReporteProductosPorMarcaCabecera()
            val reporteProductosPorMarcaList = listaCabecera.map {
                val listaDetalle = reportesRepository.getReporteProductosPorMarca(it.FirmCode)
                listOf(it.toDoReporteProductosPorMarca()) + listaDetalle
            }
            val pdf = PdfReporteProductosPorMarca(
                nombreVendedor,
                nombreSociedad,
                reporteProductosPorMarcaList,
                context)
            pdf()
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
}
