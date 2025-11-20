package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import com.mobile.massiveapp.domain.model.ReporteEstadoCuenta
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


class PdfReporteEstadoCuenta(
    private val nombreVendedor: String = "Nombre del vendedor",
    private val nombreSociedad: String,
    private val reporteEstadoCuenta: List<ReporteEstadoCuenta>,
    private val context: Context
) {

    suspend operator fun invoke():Boolean {
        val listaCobranzas = reporteEstadoCuenta.filter { it.Type == 2 }
        val listaVentas = reporteEstadoCuenta.filter { it.Type == 1 }

        var success = false
        try {

            //Se CREA LA CARPETA
            val dir = context.filesDir
            validateDir(dir)

            //Se CREA EL PDF
            val archivo = File(dir, "reporteEstadoCuenta.pdf")
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
                    "REPORTE DE ESTADO DE CUENTA \n\n"
                )
            )

            document.add(
                PdfElements.oTituloCabeceraMedium(
                    "$nombreVendedor \n"
                )
            )
            val estadoCuenta = reporteEstadoCuenta.first()

            val valoresReporteEstadoCuenta = listOf(
                "Cliente: ${estadoCuenta.CardName}\n",
                "Lista Precio: ${estadoCuenta.ListName} \n",
                "Límite Crédito: ${estadoCuenta.CreditLine} \n",
                "Condicion pago: ${estadoCuenta.PymntGroup} \n\n"
            )

            valoresReporteEstadoCuenta.forEach {
                document.add(
                    PdfElements.oTituloCabeceraNormal(it)
                )
            }

            document.add(
                PdfElements.oTituloCabeceraMedium(
                    "Ventas \n\n"
                )
            )



            if (listaVentas.isNotEmpty()){
                //Se crea la TABLA
                val tabla = PdfElements.oTabla(24)

                //Titulos Tabla
                val listaTitulos = listOf(
                    "Clave",
                    "Comprobante",
                    "Condición",
                    "Vendedor",
                    "Emisión",
                    "Moneda",
                    "Total",
                    "Saldo"
                )

                listaTitulos.forEachIndexed{index, titulo->
                    val colspan = when(index){
                        0-> 2   //Clave
                        1-> 4   //Comprobante
                        2-> 3   //Condicion
                        3-> 3   //Vendedor
                        4-> 3   //Emision
                        5-> 3   //Moneda
                        6-> 3   //Total
                        7-> 3   //Saldo
                        else -> 4
                    }

                    tabla.addCell(PdfElements.oCeldaSubTitulo(titulo, colspan))
                }

                listaVentas.forEach { reporte->
                    val fuente = PdfElements.fontShort
                    var textCelda = ""
                    var colspanCelda = 0

                    for(i in 1..8){
                        when(i){
                            1-> {
                                textCelda = reporte.DocEntry.toString()
                                colspanCelda = 2
                            }
                            2 -> {
                                textCelda = reporte.NumAtCard
                                colspanCelda = 4
                            }

                            3 -> {
                                textCelda = reporte.PymntGroup
                                colspanCelda = 3
                            }

                            4 -> {
                                textCelda = reporte.SlpName
                                colspanCelda = 3
                            }

                            5 -> {
                                textCelda = reporte.DocDate
                                colspanCelda = 3
                            }

                            6 -> {
                                textCelda = reporte.DocCur
                                colspanCelda = 3
                            }

                            7 -> {
                                textCelda = reporte.DocTotal.format(2).toString()
                                colspanCelda = 3
                            }

                            8 -> {
                                textCelda = reporte.PaidToDate.format(2).toString()
                                colspanCelda = 3
                            }

                        }

                        tabla.addCell(
                            PdfElements.oCeldaDescripcionDetalleLetraEstilo(textCelda, colspanCelda, fuente)
                        )
                    }
                }
                document.add(tabla)
                document.add(Paragraph("\n"))
            }

            val tabla = PdfElements.oTabla(24)
            tabla.addCell(PdfElements.oCeldaTituloDetalle("Total Deuda", 19))
            tabla.addCell(PdfElements.oCeldaDescripcionDetalle(getTotalDeuda().format(2).toString(), 5))
            document.add(tabla)


            document.add(
                PdfElements.oTituloCabeceraMedium(
                    "\nCobranzas \n\n"
                )
            )

            if (listaCobranzas.isNotEmpty()){


                //Se crea la TABLA
                val tablaC = PdfElements.oTabla(24)

                //Titulos Tabla
                val listaTitulos = listOf(
                    "Clave",
                    "Comprobante",
                    "Condición",
                    "Vendedor",
                    "Emisión",
                    "Fecha Pago",
                    "Número Días",
                    "Moneda",
                    "Pagado"
                )

                listaTitulos.forEachIndexed{index, titulo->
                    val colspan = when(index){
                        0-> 2   //Clave
                        1-> 3   //Comprobante
                        2-> 3   //Condicion
                        3-> 3   //Vendedor
                        4-> 3   //Emision
                        5-> 3   //Fecha Pago
                        6-> 2   //Número Días
                        7-> 2   //Moneda a Pago
                        8-> 3   //Pagado
                        else -> 4
                    }

                    tablaC.addCell(PdfElements.oCeldaSubTitulo(titulo, colspan))
                }

                listaCobranzas.forEach { reporte->
                    val fuente = PdfElements.fontShort
                    var textCelda = ""
                    var colspanCelda = 0
                    for(i in 1..9){
                        when(i){
                            1-> {
                                textCelda = reporte.DocEntry.toString()
                                colspanCelda = 2
                            }
                            2 -> {
                                textCelda = reporte.NumAtCard
                                colspanCelda = 3
                            }

                            3 -> {
                                textCelda = reporte.PymntGroup
                                colspanCelda = 3
                            }

                            4 -> {
                                textCelda = reporte.SlpName
                                colspanCelda = 3
                            }

                            5 -> {
                                textCelda = reporte.DocDate
                                colspanCelda = 3
                            }
                            6 -> {
                                textCelda = reporte.PaidDocDate
                                colspanCelda = 3
                            }
                            7 -> {
                                textCelda = reporte.PaidDays.toString()
                                colspanCelda = 2
                            }
                            8 -> {
                                textCelda = reporte.PaidDocCur
                                colspanCelda = 2
                            }
                            9 -> {
                                textCelda = reporte.PaidDocTotal.format(2).toString()
                                colspanCelda = 3
                            }
                        }
                        tablaC.addCell(
                            PdfElements.oCeldaDescripcionDetalleLetraEstilo(textCelda, colspanCelda, fuente)
                        )
                    }

                }
                document.add(tablaC)
                document.add(Paragraph("\n"))
            }
            val tabla2 = PdfElements.oTabla(24)
            var promedioDias = "0.0"
            var importe = "0.0"


            promedioDias = getPromedioDias().toString()
            importe = getImporteTotal().toString()


            tabla2.addCell(PdfElements.oCeldaTituloDetalle("Promedio días:", 10))
            tabla2.addCell(PdfElements.oCeldaDescripcionDetalle(promedioDias, 3))
            tabla2.addCell(PdfElements.oCeldaTituloDetalle("Importe:", 7))
            tabla2.addCell(PdfElements.oCeldaDescripcionDetalle(importe.format(2), 4))
            document.add(tabla2)



            //Se cierra el documento
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

    private fun getTotalDeuda(): Double =
        try {
            val listaPedidos = reporteEstadoCuenta.filter { it.Type == 1 }
            var totalDeuda = 0.0
            listaPedidos.forEach{
                totalDeuda += it.PaidToDate
            }
            totalDeuda
        } catch (e: Exception){
            e.printStackTrace()
            0.0
        }

    private fun getPromedioDias(): Double =
        try {
            val listaCobranzas = reporteEstadoCuenta.filter { it.Type == 2 }
            var promedioDias = 0.0
            listaCobranzas.forEach{
                promedioDias += it.PaidDays.toDouble()
                promedioDias /= listaCobranzas.size
            }
            promedioDias
        } catch (e: Exception){
            e.printStackTrace()
            0.0
        }
    private fun getImporteTotal(): Double =
        try {
            val listaCobranzas = reporteEstadoCuenta.filter { it.Type == 2 }
            var importeTotal = 0.0
            if (listaCobranzas.isNotEmpty()){
                importeTotal = listaCobranzas.sumOf { it.PaidDocTotal }
            }
            importeTotal
        } catch (e: Exception){
            e.printStackTrace()
            0.0
        }

}