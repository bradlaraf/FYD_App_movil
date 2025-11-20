package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import com.mobile.massiveapp.domain.model.ReporteAvanceVentas
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.validateDir
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class PdfReporteAvanceDeVentas  (
    private val nombreVendedor: String,
    private val nombreSociedad: String,
    private val listaAvanceVentas : Map<String, List<ReporteAvanceVentas>>,
    private val context: Context
) {

    suspend operator fun invoke():Boolean {
        var success = false
        try {

            //Se CREA LA CARPETA
            val dir = context.filesDir
            validateDir(dir)

            //Se CREA EL PDF
            val archivo = File(dir, "reporteAvanceDeVentas.pdf")
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
                    "REPORTE DE AVANCE DE VENTAS \n\n"
                )
            )

            document.add(
                PdfElements.oTituloCabeceraNormal(
                    "$nombreVendedor \n\n"
                )
            )

            listaAvanceVentas.forEach { producto, listaReporteVenta->
                val sizeTabla = 10
                val tablaReporte = PdfElements.oTabla(sizeTabla)
                val cantidadTotal = listaReporteVenta.sumOf { it.Quantity }
                val montoTotal = listaReporteVenta.sumOf { it.LineTotal.toDoubleOrNull()?:0.0 }

                listaReporteVenta.forEachIndexed {index, reporteVenta->

                    val celdaTipo = PdfElements.oCeldaDescripcionDetalleCenter(reporteVenta.Indicator,1)
                    val celdaCodido = PdfElements.oCeldaDescripcionDetalleCenter(reporteVenta.NumAtCard, 2)
                    val celdaArticulo = PdfElements.oCeldaDescripcionDetalleCenter(reporteVenta.ItemName, 4)
                    val celdaCantidad = PdfElements.oCeldaDescripcionDetalleCenter(reporteVenta.Quantity.toString(), 1)
                    val celdaImporte = PdfElements.oCeldaDescripcionDetalleCenter(reporteVenta.LineTotal.format(2).toString(), 2)


                        //Set CABECERA DE LA TABLA
                    if( index == 0 ){
                        /*celdaCodido = PdfElements.oCeldaTituloGrande("Marca: ", 2)
                        celdaArticulo = PdfElements.oCeldaTituloGrande(reporteVenta.FirmName, 4)
                        celdaImporte = PdfElements.oCeldaTituloGrande(montoTotal.format(2).toString(), 2)
                        celdaCantidad = PdfElements.oCeldaTituloGrande(cantidadTotal.toString(), 2)*/

                        tablaReporte.addCell(PdfElements.oCeldaTituloGrande("Marca: ", 2))
                        tablaReporte.addCell(PdfElements.oCeldaTituloGrande(reporteVenta.FirmName, 4))
                        tablaReporte.addCell(PdfElements.oCeldaTituloGrande(cantidadTotal.toString(), 2))
                        tablaReporte.addCell(PdfElements.oCeldaTituloGrande(montoTotal.format(2).toString(), 2))

                        val titulosCabecera = listOf("Tipo","Comprobante", "Articulo", "Cant.", "Importe")
                        titulosCabecera.forEachIndexed { index, titulo ->
                            when (titulo) {
                                "Articulo" -> {
                                    tablaReporte.addCell(
                                        PdfElements.oCeldaSubTitulo(titulo, 4)
                                    )
                                }
                                "Comprobante" -> {
                                    tablaReporte.addCell(
                                        PdfElements.oCeldaSubTitulo(titulo, 2)
                                    )
                                }
                                "Importe" -> {
                                    tablaReporte.addCell(
                                        PdfElements.oCeldaSubTitulo(titulo, 2)
                                    )
                                }
                                else -> {
                                    tablaReporte.addCell(
                                        PdfElements.oCeldaSubTitulo(titulo, 1)
                                    )
                                }
                            }
                        }

                        tablaReporte.addCell(celdaTipo)
                        tablaReporte.addCell(celdaCodido)
                        tablaReporte.addCell(celdaArticulo)
                        tablaReporte.addCell(celdaCantidad)
                        tablaReporte.addCell(celdaImporte)

                    } else {
                        tablaReporte.addCell(celdaTipo)
                        tablaReporte.addCell(celdaCodido)
                        tablaReporte.addCell(celdaArticulo)
                        tablaReporte.addCell(celdaCantidad)
                        tablaReporte.addCell(celdaImporte)
                    }

                }
                document.add(Paragraph("\n\n"))
                document.add(tablaReporte)
            }

            val tablaT = PdfElements.oTabla(10)
            var total = 0.0
            var cantidad = 0.0
            listaAvanceVentas.forEach { marca, lista->
                lista.forEach {
                    total += it.LineTotal.toDoubleOrNull()?:0.0
                    cantidad += it.Quantity
                }
            }

            document.add(Paragraph("\n"))
            tablaT.addCell(
                PdfElements.oCeldaDescripcionDetalleLetraCustom("Cantidad Total: ", 4, PdfElements.fontNegrita,
                    Element.ALIGN_RIGHT
                )
            )

            tablaT.addCell(
                PdfElements.oCeldaDescripcionDetalleLetraCustom(cantidad.format(2).toString(), 2, PdfElements.fontNormal,
                    Element.ALIGN_RIGHT
                )
            )

            document.add(Paragraph("\n"))
            tablaT.addCell(
                PdfElements.oCeldaDescripcionDetalleLetraCustom("Monto Total: ", 2, PdfElements.fontNegrita,
                    Element.ALIGN_RIGHT
                )
            )

            tablaT.addCell(
                PdfElements.oCeldaDescripcionDetalleLetraCustom(total.format(2).toString(), 2, PdfElements.fontNormal,
                    Element.ALIGN_RIGHT
                )
            )

            document.add(tablaT)
            document.add(Paragraph("\n"))


            //Cierre del documento
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