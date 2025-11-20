package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import com.mobile.massiveapp.domain.model.ReporteDetalleVenta
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.validateDir
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class PdfReporteDetalleDeVentas(
    private val nombreVendedor: String,
    private val nombreSociedad: String,
    private val listaDetalleVentas: Map<String, List<ReporteDetalleVenta>>,
    private val context: Context
) {

    suspend operator fun invoke(): Boolean {
        var success = false
        try {

            //Se CREA LA CARPETA
            val dir = context.filesDir
            validateDir(dir)

            //Se CREA EL PDF
            val archivo = File(dir, "reporteDetalleDeVentas.pdf")
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
                    "REPORTE DE DETALLE DE VENTAS \n\n"
                )
            )

            document.add(
                PdfElements.oTituloCabeceraMedium(
                    "$nombreVendedor \n"
                )
            )

            listaDetalleVentas.forEach { numAtCard, listaDetalle ->
                val tabla = PdfElements.oTabla(66)
                val montoTotal = listaDetalle.sumOf { it.LineTotal }

                listaDetalle.forEachIndexed { index, reporte ->


                    //CABECERA
                    if (index == 0) {
                        tabla.addCell(PdfElements.oCeldaTituloMediano("Interno: ${reporte.DocEntry}", 22))
                        tabla.addCell(PdfElements.oCeldaTituloMediano("Comprobante: \n ${reporte.Indicator} ${reporte.NumAtCard}", 22))
                        tabla.addCell(PdfElements.oCeldaTituloMediano("Fecha: ${reporte.DocDate}", 22))

                        tabla.addCell(PdfElements.oCeldaSubTitulo("Cliente: ${reporte.CardName}", 33))
                        tabla.addCell(PdfElements.oCeldaSubTitulo("Importe: ${reporte.DocCur} ${montoTotal.format(2)}", 33))

                        val titulos = listOf(
                            "Nro",
                            "Codigo",
                            "Articulo",
                            "Unidad",
                            "Cantidad",
                            "Tipo precio",
                            "Precio",
                            "Parcial"
                        )

                        titulos.forEachIndexed { indx, it ->
                            var colspan = 0
                            colspan =
                                when (indx) {
                                    0 -> 5
                                    1, 3, 5 -> 9
                                    2 -> 15
                                    4 -> 7
                                    else -> 6
                                }
                            tabla.addCell(PdfElements.oCeldaSubTitulo(it, colspan))
                        }

                        val fuente = PdfElements.fontShort
                        var textCelda = ""
                        var colspanCelda = 0
                        for(i in 1..8){
                            when(i){
                                1-> {
                                    textCelda = "${index + 1}"
                                    colspanCelda = 5
                                }
                                2 -> {
                                    textCelda = reporte.ItemCode
                                    colspanCelda = 9
                                }

                                3 -> {
                                    textCelda = reporte.Dscription
                                    colspanCelda = 15
                                }

                                4 -> {
                                    textCelda = reporte.UnitMsr
                                    colspanCelda = 9
                                }

                                5 -> {
                                    textCelda = reporte.Quantity.toString()
                                    colspanCelda = 7
                                }

                                6 -> {
                                    textCelda = reporte.PriceList
                                    colspanCelda = 9
                                }

                                7 -> {
                                    textCelda = reporte.Price.format(6).toString()
                                    colspanCelda = 6
                                }

                                8 -> {
                                    textCelda = reporte.LineTotal.format(6).toString()
                                    colspanCelda = 6
                                }

                            }

                            tabla.addCell(
                                PdfElements.oCeldaDescripcionDetalleLetraEstilo(textCelda, colspanCelda, fuente)
                            )
                        }


                        //DETALLES
                    } else {
                        val fuente = PdfElements.fontShort
                        var textCelda = ""
                        var colspanCelda = 0
                        for(i in 1..8){
                            when(i){
                                1-> {
                                    textCelda = "${index + 1}"
                                    colspanCelda = 5
                                }
                                2 -> {
                                    textCelda = reporte.ItemCode
                                    colspanCelda = 9
                                }

                                3 -> {
                                    textCelda = reporte.Dscription
                                    colspanCelda = 15
                                }

                                4 -> {
                                    textCelda = reporte.UnitMsr
                                    colspanCelda = 9
                                }

                                5 -> {
                                    textCelda = reporte.Quantity.toString()
                                    colspanCelda = 7
                                }

                                6 -> {
                                    textCelda = reporte.PriceList
                                    colspanCelda = 9
                                }

                                7 -> {
                                    textCelda = reporte.Price.format(6).toString()
                                    colspanCelda = 6
                                }

                                8 -> {
                                    textCelda = reporte.LineTotal.format(6).toString()
                                    colspanCelda = 6
                                }

                            }

                            tabla.addCell(
                                PdfElements.oCeldaDescripcionDetalleLetraEstilo(textCelda, colspanCelda, fuente)
                            )
                        }
                    }


                }
                document.add(Paragraph("\n\n"))
                document.add(tabla)
            }

            success = true
            document.close()

            return success
        } catch (e: FileNotFoundException){
            e.printStackTrace()
            return success
        } catch (e: DocumentException){
            e.printStackTrace()
            return success
        } catch (e: Exception){
            e.printStackTrace()
            return success
        }
    }

}