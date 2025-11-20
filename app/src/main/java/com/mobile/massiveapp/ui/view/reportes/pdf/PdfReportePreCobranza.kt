package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import com.mobile.massiveapp.domain.model.ReportePreCobranza
import com.mobile.massiveapp.ui.view.util.format
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.mobile.massiveapp.ui.view.util.validateDir
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Element.ALIGN_RIGHT
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class PdfReportePreCobranza  (
    private val nombreVendedor: String,
    val nombreSociedad: String,
    private val reportePreCobranza : List<ReportePreCobranza>,
    private val context: Context
) {

    val CSPN_CLAVE = 2
    val CSPN_COMPROBANTE = 5
    val CSPN_TIPO = 2
    val CSPN_CLIENTE = 6
    val CSPN_FECHA_DOC = 3
    val CSPN_FECHA_PAGO = 3
    val CSPN_IMPORTE = 3
    val CSPN_COBRADO = 3
    val CSPN_SALDO = 3
    val CSPN_RECIBO = 3

    private fun getSaldo(reporte: ReportePreCobranza): Double {
        return (reporte.DocTotal.format(2) - reporte.PaidToDate.format(2))
    }


    suspend operator fun invoke():Boolean {
        var total = 0.0
        var success = false
        try {

            //Se CREA LA CARPETA
            val dir = context.filesDir
            validateDir(dir)


            //Se CREA EL PDF
            val archivo = File(dir, "reportePreCobranza.pdf")

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
                    "REPORTE DE PRE COBRANZA \n\n"
                )
            )

            document.add(
                PdfElements.oTituloCabeceraMedium(
                    "$nombreVendedor \n\n"
                )
            )

            if (reportePreCobranza.isNotEmpty()){
                val CSPN_TIPO_PAGO = 22
                val CSPN_TOTAL_PAGOS = 11



                val listaElementos = listOf(
                    CSPN_CLAVE,
                    CSPN_COMPROBANTE,
                    CSPN_TIPO,
                    CSPN_CLIENTE,
                    CSPN_FECHA_DOC,
                    CSPN_FECHA_PAGO,
                    CSPN_IMPORTE,
                    CSPN_COBRADO,
                    CSPN_SALDO,
                    CSPN_RECIBO
                )

                reportePreCobranza.forEachIndexed { index, reportePreCobranza->
                    total += reportePreCobranza.PayDocTotal
                    //Se crea la TABLA
                    val tabla = PdfElements.oTabla(listaElementos.sumOf { it })

                    //Titulos Tabla
                    /*val listaTitulos = mapOf(
                        "Tipo Pago: $tipoPago" to CSPN_TIPO_PAGO,
                        "Total: ${reportePreCobranza.sumOf { it.PayDocTotal }.format(2)}" to CSPN_TOTAL_PAGOS
                    )

                    listaTitulos.forEach{titulo, colspan->
                        tabla.addCell(PdfElements.oCeldaTituloGrande(titulo, colspan))
                    }*/

                    if (index == 0){
                        val listaSubTitulos = mapOf(
                            "Clave" to CSPN_CLAVE,
                            "Tipo" to CSPN_TIPO,
                            "Comprobante" to CSPN_COMPROBANTE,
                            "Emisión" to CSPN_FECHA_DOC,
                            "Fecha pago" to CSPN_FECHA_PAGO,
                            "Cliente" to CSPN_CLIENTE,
                            "Importe" to CSPN_IMPORTE,
                            "Cobrado" to CSPN_COBRADO,
                            "Saldo" to CSPN_SALDO,
                            "Recibo" to CSPN_RECIBO
                        )

                        listaSubTitulos.forEach{titulo, colspan->
                            tabla.addCell(PdfElements.oCeldaSubTitulo(titulo, colspan))
                        }
                    }


                    /*reportePreCobranza.forEach { reporte->}*/
                    val fuente = PdfElements.fontShort
                    val listaContenido = mapOf(
                        reportePreCobranza.DocEntry.toString() to CSPN_CLAVE,
                        reportePreCobranza.Indicator to CSPN_TIPO,
                        reportePreCobranza.NumAtCard to CSPN_COMPROBANTE,
                        reportePreCobranza.DocDate to CSPN_FECHA_DOC,
                        reportePreCobranza.PayDate to CSPN_FECHA_PAGO,
                        reportePreCobranza.CardName to CSPN_CLIENTE,
                        reportePreCobranza.DocTotal.format(2).toString() to CSPN_IMPORTE,
                        reportePreCobranza.PaidToDate.format(2).toString() to CSPN_COBRADO,
                        getSaldo(reportePreCobranza).format(2).toString() to CSPN_SALDO,
                        reportePreCobranza.PayDocTotal.format(2).toString() to CSPN_RECIBO
                    )

                    val listatest = listOf(
                        reportePreCobranza.DocEntry.toString(),
                        reportePreCobranza.Indicator,
                        reportePreCobranza.NumAtCard,
                        reportePreCobranza.DocDate,
                        reportePreCobranza.PayDate,
                        reportePreCobranza.CardName,
                        reportePreCobranza.DocTotal.format(2).toString(),
                        reportePreCobranza.PaidToDate.format(2).toString(),
                        getSaldo(reportePreCobranza).format(2).toString(),
                        reportePreCobranza.PayDocTotal.format(2).toString()
                    )

                    listatest.forEachIndexed {index, text->
                        val tetet = when(index){
                            0->{CSPN_CLAVE}
                            1->{CSPN_TIPO}
                            2->{CSPN_COMPROBANTE}
                            3->{CSPN_FECHA_DOC}
                            4->{CSPN_FECHA_PAGO}
                            5->{CSPN_CLIENTE}
                            6->{CSPN_IMPORTE}
                            7->{CSPN_COBRADO}
                            8->{CSPN_SALDO}
                            else ->3
                        }

                        tabla.addCell(PdfElements.oCeldaDescripcionDetalleLetraEstilo(text, tetet, fuente))
                    }
                    document.add(tabla)
                }

                val tablaT = PdfElements.oTabla(33)
                    document.add(Paragraph("\n"))
                    tablaT.addCell(
                        PdfElements.oCeldaDescripcionDetalleLetraCustom("Total: ", 26, PdfElements.fontNegrita, ALIGN_RIGHT)
                    )

                    tablaT.addCell(
                        PdfElements.oCeldaDescripcionDetalleLetraCustom(total.format(2).toString(), 7, PdfElements.fontNegrita, ALIGN_RIGHT)
                    )

                document.add(tablaT)
                document.add(Paragraph("\n"))

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