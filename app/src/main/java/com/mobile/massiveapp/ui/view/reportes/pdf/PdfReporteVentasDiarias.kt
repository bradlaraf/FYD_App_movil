package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import com.mobile.massiveapp.domain.model.ReporteVentasDiarias
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

class PdfReporteVentasDiarias  (
    private val nombreVendedor: String,
    private val nombreSociedad: String,
    private val listaVentasDiarias: Map<String, List<ReporteVentasDiarias>>,
    private val context: Context
) {
    suspend operator fun invoke():Boolean {
        var success = false
        try {

            //Se CREA LA CARPETA
            val dir = context.filesDir
            validateDir(dir)

            //Se CREA EL PDF
            val archivo = File(dir, "reporteVentasDiarias.pdf")
            val fos = withContext(Dispatchers.IO) {
                FileOutputStream(archivo)
            }

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
                    "REPORTE DE VENTAS DIARIAS \n\n"
                )
            )

            document.add(
                PdfElements.oTituloCabeceraNormal(
                    "$nombreVendedor \n\n"
                )
            )

            if (listaVentasDiarias.isNotEmpty()){

                listaVentasDiarias.forEach{ fecha, listaReporte->
                    val totalVentaXFecha = listaReporte.sumOf { it.DocTotal }

                    listaReporte.forEachIndexed { index, reporte->
                        val tabla = PdfElements.oTabla(18)

                        if (index == 0){
                            tabla.addCell(PdfElements.oCeldaTituloGrande("Fecha: $fecha",9))
                            tabla.addCell(PdfElements.oCeldaTituloGrande("Total: ${totalVentaXFecha.format(2)}",9))

                            val titulos = listOf(
                                "Nro",
                                "Interno",
                                "Tipo",
                                "Comprobante",
                                "Importe",
                                "Cliente"
                            )

                            titulos.forEachIndexed { indx, titulo->
                                val colspan = when(indx){
                                    0 -> 1
                                    1 -> 2
                                    2 -> 2 //Tipo
                                    3 -> 4 //Comprobante
                                    4 -> 3
                                    5 -> 6
                                    else -> 1
                                }
                                tabla.addCell(PdfElements.oCeldaSubTitulo(titulo, colspan))
                            }
                        }
                        val fuente = PdfElements.fontShort
                        var textCelda = ""
                        var colspanCelda = 0
                        for(i in 1..6) {
                            when (i) {
                                1 -> {
                                    textCelda = (index+1).toString()
                                    colspanCelda = 1
                                }

                                2 -> {
                                    textCelda = reporte.DocNum.toString()
                                    colspanCelda = 2
                                }

                                3 -> {
                                    textCelda = reporte.Indicator
                                    colspanCelda = 2
                                }

                                4 -> {
                                    textCelda = reporte.NumAtCard
                                    colspanCelda = 4
                                }

                                5 -> {
                                    textCelda = reporte.DocTotal.format(2).toString()
                                    colspanCelda = 3
                                }

                                6 -> {
                                    textCelda = reporte.CardName
                                    colspanCelda = 6
                                }
                            }
                            tabla.addCell(
                                PdfElements.oCeldaDescripcionDetalleLetraEstilo(
                                    textCelda,
                                    colspanCelda,
                                    fuente
                                )
                            )
                        }
                        document.add(tabla)
                    }
                    document.add(Paragraph("\n\n"))

                }
            }

            success = true
            document.close()

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