package com.mobile.massiveapp.ui.view.reportes.pdf

import android.content.Context
import android.os.Environment
import com.mobile.massiveapp.data.model.ClientePedidos
import com.mobile.massiveapp.ui.view.util.getFechaActual
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class PdfGenerator(
    private val listaDatos :List<Any>,
    private val isSuccess:(Boolean)->Unit
) {
    private val carpeta = "/reportesPdf"
    private val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + carpeta


    fun crearPdfInternalStorage(context: Context){
        val filename = "reporte.pdf"
        val directory = File(context.filesDir, filename)

        if (!directory.exists()){
            directory.mkdir()
        }


    }

    suspend operator fun invoke() {
        try {
                //Se CREA LA CARPETA
            val dir = File(path)
            if (!dir.exists()){
                dir.mkdirs()
            }

                //Se CREA EL PDF
            val archivo = File(dir, "reporte.pdf")
            val fos = withContext(Dispatchers.IO) {
                FileOutputStream(archivo)
            }

            val document = Document()
            PdfWriter.getInstance(document, fos)

            document.open()

            val titulo1 = Paragraph(
                "Empresa: Productos Alimentos Golosinas S.A.C \n",
                FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
            )
            val titulo2 = Paragraph(
                "Dirección: Calle Tacna N°330-Iquitos-Maynas-Loreto \n",
                FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
            )
            val titulo3 = Paragraph(
                "Fecha de impresión: ${getFechaActual()} \n\n",
                FontFactory.getFont("arial", 14f, Font.BOLD, BaseColor.BLACK)
            )

            val tituloTipoReporte = Paragraph(
                "REPORTE DE PEDIDOS \n\n",
                FontFactory.getFont("arial", 16f, Font.BOLD, BaseColor.BLACK)
            )

            tituloTipoReporte.alignment = Paragraph.ALIGN_LEFT
            titulo3.alignment = Paragraph.ALIGN_LEFT
            titulo1.alignment = Paragraph.ALIGN_LEFT
            titulo2.alignment = Paragraph.ALIGN_LEFT

            document.add(titulo1)
            document.add(titulo2)
            document.add(titulo3)
            document.add(tituloTipoReporte)

                //Se crea la TABLA
            val tabla = PdfPTable(4)
            val fontNegrita = FontFactory.getFont("arial", 12f, Font.BOLD, BaseColor.BLACK)
            val fontNormal = FontFactory.getFont("arial", 12f, Font.NORMAL, BaseColor.BLACK)
            val listaTitulos = listOf("Cliente", "Fecha", "Monto Impuesto", "Monto Pedido")


            if (listaDatos.isNotEmpty()){
                when (listaDatos[0]) {
                    is ClientePedidos -> {
                        val listaDatosPedidos = listaDatos as List<ClientePedidos>
                        listaTitulos.forEach {
                            val tituloCell = PdfPCell(Phrase(Chunk(it, fontNegrita)))
                            tituloCell.verticalAlignment = PdfPCell.ALIGN_CENTER
                            tituloCell.horizontalAlignment = PdfPCell.ALIGN_CENTER
                            tituloCell.setPadding(5f)
                            tabla.addCell(tituloCell)
                        }


                        listaDatosPedidos.forEach {
                            tabla.addCell(it.CardName)
                            tabla.addCell(it.DocDate)
                            tabla.addCell("0")
                            tabla.addCell(it.DocTotal.toString())
                        }

                        document.add(tabla)
                        document.close()
                        isSuccess(true)
                    }

                    else->{
                        tabla.addCell("Cliente")
                        tabla.addCell("Fecha")
                        tabla.addCell("Monto Impuesto")
                        tabla.addCell("Monto Pedido")

                        document.add(tabla)
                        document.close()
                        isSuccess(true)
                    }
                }
            }

        } catch (e:FileNotFoundException){
            e.printStackTrace()
            Timber.tag("pdf").d(e)
            isSuccess(false)
        } catch (e:DocumentException){
            e.printStackTrace()
            Timber.tag("pdf").d(e)
            isSuccess(false)
        }
    }

}