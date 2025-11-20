package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import androidx.core.content.ContextCompat.getExternalFilesDirs
import com.mobile.massiveapp.domain.model.ReporteSaldosPorCobrar
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.validateDir
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class PdfReporteSaldosPorCobrar (
    private val nombreVendedor: String,
    private val nombreSociedad: String,
    private val reporteSaldosPorCobrar: List<ReporteSaldosPorCobrar>,
    private val context: Context
) {

    suspend operator fun invoke():Boolean {
        val path = getExternalFilesDirs(context, null)

        var success = false
        try {

            val dir = context.filesDir
            validateDir(dir)

            val archivo = File(dir, "reporteSaldosPorCobrar.pdf")
            val fos = withContext(Dispatchers.IO) {
                FileOutputStream(archivo)
            }

            // Establecer el ancho y alto de la página manualmente (en puntos)
            val pageWidth = 650f // Ancho para A4
            val pageHeight = 842f // Alto para A4
            val marginLeft = 15f
            val marginRight = 15f
            val marginTop = 20f
            val marginBottom = 20f

            val document = Document()
            PdfWriter.getInstance(document, fos)

            document.open()

            //CABECERA DOCUMENTO

            val listaTitulosTabla = listOf(
                "Empresa: $nombreSociedad \n",
                "Dirección: Calle Tacna N°330-Iquitos-Maynas-Loreto \n",
                "Fecha de impresión: ${getFechaActual()} \n\n"
            )

            listaTitulosTabla.forEach {
                document.add(
                    PdfElements.oTituloCabeceraNormal(it)
                )
            }

            document.add(
                PdfElements.oTituloCabeceraLarge(
                    "REPORTE DE SALDOS POR VENDEDOR \n\n"
                )
            )

            document.add(
                PdfElements.oTituloCabeceraNormal(
                    "$nombreVendedor \n\n"
                )
            )

            val CSPN_CLAVE = 3
            val CSPN_COMPROBANTE = 4
            val CSPN_EMISION = 3
            val CSPN_DIAS = 2
            val CSPN_NOMBRE = 6
            val CSPN_DIRECCION = 6
            val CSPN_TOTAL = 2
            val CSPN_PAGADO = 2
            val CSPN_SALDO = 2


                reporteSaldosPorCobrar.forEachIndexed { indxx, reporte->
                    val tabla = PdfElements.oTabla(30)
                    if (indxx == 0){
                        val titulos = mapOf(
                            "Clave" to CSPN_CLAVE,
                            "Comprobante" to CSPN_COMPROBANTE,
                            "Emision" to CSPN_EMISION,
                            "Dias" to CSPN_DIAS,
                            "Nombre" to CSPN_NOMBRE,
                            "Direccion" to CSPN_DIRECCION,
                            "Total" to CSPN_TOTAL,
                            "Pagado" to CSPN_PAGADO,
                            "Saldo" to CSPN_SALDO
                        )

                        titulos.forEach { nombre, colspan->
                            val estilos = PdfElements.fontShortBold
                            tabla.addCell(
                                PdfElements.oCeldaTituloLetraEstilo(nombre, colspan, estilos)
                            )
                        }
                        document.add(tabla)
                    } else {
                        val tablaContent = PdfElements.oTabla(30)
                        //Contenido
                        val fuente = PdfElements.fontShort

                        /*  val content= mapOf(
                            reporte.DocNum.toString() to CSPN_CLAVE,
                            reporte.NumAtCard to CSPN_COMPROBANTE,
                            reporte.DocDate to CSPN_EMISION,
                            reporte.Days.toString() to CSPN_DIAS,
                            reporte.CardName to CSPN_NOMBRE,
                            reporte.Address to CSPN_DIRECCION,
                            reporte.DocTotal.format(2).toString() to CSPN_TOTAL,
                            (reporte.DocTotal - reporte.PaidToDate).format(2).toString() to CSPN_PAGADO,
                            reporte.PaidToDate.format(2).toString() to CSPN_SALDO
                        )*/

                        var textCelda = ""
                        var colspanCelda = 0
                        for(i in 1..9) {
                            when (i) {
                                1 -> {
                                    textCelda = reporte.DocNum.toString()
                                    colspanCelda = CSPN_CLAVE
                                }

                                2 -> {
                                    textCelda = reporte.NumAtCard
                                    colspanCelda = CSPN_COMPROBANTE
                                }

                                3 -> {
                                    textCelda = reporte.DocDate
                                    colspanCelda = CSPN_EMISION
                                }

                                4 -> {
                                    textCelda = reporte.Days.toString()
                                    colspanCelda = CSPN_DIAS
                                }

                                5 -> {
                                    textCelda = reporte.CardName
                                    colspanCelda = CSPN_NOMBRE
                                }

                                6 -> {
                                    textCelda = reporte.Address
                                    colspanCelda = CSPN_DIRECCION
                                }

                                7 -> {
                                    textCelda =  reporte.DocTotal.format(2).toString()
                                    colspanCelda = CSPN_TOTAL
                                }

                                8 -> {
                                    textCelda = (reporte.DocTotal - reporte.PaidToDate).format(2).toString()
                                    colspanCelda = CSPN_PAGADO
                                }

                                9 -> {
                                    textCelda = reporte.PaidToDate.format(2).toString()
                                    colspanCelda = CSPN_SALDO
                                }
                            }
                            tablaContent.addCell(
                                PdfElements.oCeldaDescripcionDetalleLetraEstilo(
                                    textCelda,
                                    colspanCelda,
                                    fuente
                                )
                            )
                        }
                        /*content.forEach { nombre, colspan ->
                            tablaContent.addCell(
                                PdfElements.oCeldaDescripcionDetalleLetraEstilo(nombre, colspan, fuente)
                            )
                        }*/

                        document.add(tablaContent)
                    }
                }





            if (reporteSaldosPorCobrar.isNotEmpty()){
                document.add(Paragraph("\n"))

                val totalDoc = reporteSaldosPorCobrar.sumOf { it.DocTotal }
                val totalPagado = reporteSaldosPorCobrar.sumOf { (it.DocTotal - it.PaidToDate) }
                val totalSaldo = reporteSaldosPorCobrar.sumOf { it.PaidToDate }

                val tabla = PdfElements.oTabla(30)
                tabla.addCell(
                    PdfElements.oCeldaTituloDetalle(
                        "Total general:",
                        21
                    )
                )
                tabla.addCell(
                    PdfElements.oCeldaDescripcionDetalleLetraEstilo(
                        totalDoc.format(2).toString(),
                        3,
                        PdfElements.fontShort
                    )
                )

                tabla.addCell(
                    PdfElements.oCeldaDescripcionDetalleLetraEstilo(
                        totalPagado.format(2).toString(),
                        3,
                        PdfElements.fontShort
                    )
                )
                tabla.addCell(
                    PdfElements.oCeldaDescripcionDetalleLetraEstilo(
                        totalSaldo.format(2).toString(),
                        3,
                        PdfElements.fontShort
                    )
                )
                document.add(tabla)
            }

            success = true
            document.close()
            withContext(Dispatchers.IO) {
                fos.close()
            }

            return success

        } catch (e: FileNotFoundException){
            Timber.tag("CreacionPdf").d(e)
            return success
        } catch (e: DocumentException){
            Timber.tag("CreacionPdf").d(e)
            return success
        } catch (e: Exception){
            Timber.tag("CreacionPdf").d(e)
            return success
        }
    }

}