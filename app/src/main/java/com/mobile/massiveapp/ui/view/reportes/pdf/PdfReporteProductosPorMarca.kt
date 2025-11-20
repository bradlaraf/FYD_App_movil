package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import com.mobile.massiveapp.domain.model.DoReporteProductosPorMarca
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

class PdfReporteProductosPorMarca (
    private val nombreVendedor: String,
    private val nombreSociedad: String,
    private val listaProductosPorMarca : List<List<DoReporteProductosPorMarca>>,
    private val context: Context
) {

    suspend operator fun invoke():Boolean {
        var success = false
        try {

                //Se CREA LA CARPETA
            val dir = context.filesDir
            validateDir(dir)

                //Se CREA EL PDF
            val archivo = File(dir, "reporteProductosPorMarca.pdf")
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
                    "REPORTE DE PRODUCTOS POR MARCA \n\n"
                )
            )

            document.add(
                PdfElements.oTituloCabeceraMedium(
                    "$nombreVendedor \n\n"
                )
            )

            listaProductosPorMarca.forEach { listaDetalle ->
                val tabla = PdfElements.oTabla(66)
                listaDetalle.forEachIndexed { index, reporte ->


                    //CABECERA
                    if (index == 0) {
                        val tituloCabecera = PdfElements.oTituloCabeceraMedium(reporte.Producto)
                        document.add(tituloCabecera)
                        document.add(Paragraph("\n"))

                        val titulos = listOf(
                            "Producto",
                            "Stock",
                            "Precio Cobertura",
                            "Precio Mayorista"
                        )

                        titulos.forEachIndexed { indx, it ->
                            var colspan = 0
                            colspan =
                                when (indx) {
                                    0 -> 34
                                    1 -> 8
                                    2 -> 12
                                    3 -> 12
                                    else -> 6
                                }
                            tabla.addCell(PdfElements.oCeldaSubTitulo(it, colspan))
                        }
                        //DETALLES
                    } else {
                        val fuente = PdfElements.fontShort
                        var textCelda = ""
                        var colspanCelda = 0
                        for(i in 1..4){
                            when(i){
                                1-> {
                                    textCelda = reporte.Producto
                                    colspanCelda = 34
                                }
                                2 -> {
                                    textCelda = reporte.Stock.format(6).toString()
                                    colspanCelda = 8
                                }

                                3 -> {
                                    textCelda = reporte.PrecioCobertura.format(2).toString()
                                    colspanCelda = 12
                                }

                                4 -> {
                                    textCelda = reporte.PrecioMayorista.format(2).toString()
                                    colspanCelda = 12
                                }


                            }

                            tabla.addCell(
                                PdfElements.oCeldaDescripcionDetalleLetraEstilo(textCelda, colspanCelda, fuente)
                            )
                        }
                    }
                }
                document.add(tabla)
                document.add(Paragraph("\n\n"))
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