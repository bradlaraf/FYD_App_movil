package com.mobile.massiveapp.ui.view.reportes.pdf

import android.os.Environment
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Element.ALIGN_MIDDLE
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable

class PdfElements {

    companion object {

        //No se usa
        private const val CARPETA = "/reportesPragsaPdf"
        val PATH = Environment.getExternalStorageDirectory().absolutePath + CARPETA
        //No se usa

        val fontNegrita = FontFactory.getFont(
            "arial",
            10f,
            Font.BOLD,
            BaseColor.BLACK
        )

        val fontNegritaMedium = FontFactory.getFont(
            "arial",
            13f,
            Font.BOLD,
            BaseColor.BLACK
        )

        val fontNegritaLarge = FontFactory.getFont(
            "arial",
            16f,
            Font.BOLD,
            BaseColor.BLACK
        )

        val fontNegritaLargeGrayColor = FontFactory.getFont(
            "arial",
            16f,
            Font.BOLD,
            BaseColor.GRAY
        )

        val fontNormal = FontFactory.getFont(
            "arial",
            12f,
            Font.NORMAL,
            BaseColor.BLACK
        )

        val fontShort = FontFactory.getFont(
            "arial",
            8f,
            Font.NORMAL,
            BaseColor.BLACK
        )

        val fontShortBold = FontFactory.getFont(
            "arial",
            8f,
            Font.BOLD,
            BaseColor.BLACK
        )



        fun oTabla(numeroColumnas: Int): PdfPTable {
            val tabla = PdfPTable(numeroColumnas)
            tabla.paddingTop = 2.25f
            tabla.totalWidth = 540f
            tabla.isLockedWidth = true
            return tabla
        }

        fun oTituloCabeceraNormal(titulo: String): Paragraph {
            val title = Paragraph(titulo, fontNegrita)
            title.alignment = Paragraph.ALIGN_LEFT
            return title
        }

        fun oTituloCabeceraMedium(titulo: String): Paragraph {
            val title = Paragraph(titulo, fontNegritaMedium)
            title.alignment = Paragraph.ALIGN_LEFT
            return title
        }

        fun oTituloCabeceraLarge(titulo: String): Paragraph {
            val title = Paragraph(titulo, fontNegritaLarge)
            title.alignment = Paragraph.ALIGN_LEFT
            return title
        }

        fun oCeldaTitulo(titulo: String, colspan: Int = 1): PdfPCell {
            val celda = PdfPCell(Phrase(Chunk(titulo, fontNegrita)))
            celda.colspan = colspan
            celda.paddingBottom = 3f
            celda.paddingTop = 3f
            celda.paddingLeft = 2.25f
            celda.paddingRight = 2.25f
            celda.verticalAlignment = PdfPCell.ALIGN_CENTER
            celda.horizontalAlignment = PdfPCell.ALIGN_RIGHT
            return celda
        }

        fun oCeldaTituloMediano(titulo:String, colspan: Int = 1):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fontNegritaMedium)))
            celda.colspan = colspan
            celda.paddingBottom = 6f
            celda.paddingTop = 4f
            celda.paddingLeft = 4f
            celda.paddingRight = 4f
            celda.verticalAlignment = PdfPCell.ALIGN_MIDDLE
            celda.horizontalAlignment = PdfPCell.ALIGN_CENTER
            return celda
        }

        fun oCeldaTituloGrande(titulo:String, colspan: Int = 1):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fontNegritaLargeGrayColor)))
            celda.colspan = colspan
            celda.paddingBottom = 6f
            celda.paddingTop = 4f
            celda.paddingLeft = 4f
            celda.paddingRight = 4f
            celda.verticalAlignment = PdfPCell.ALIGN_MIDDLE
            celda.horizontalAlignment = PdfPCell.ALIGN_CENTER
            return celda
        }

        fun oCeldaTituloDetalle(titulo:String, colspan: Int = 1):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fontNegrita)))
            celda.colspan = colspan
            celda.paddingTop = 2.5f
            celda.verticalAlignment = PdfPCell.ALIGN_CENTER
            celda.horizontalAlignment = PdfPCell.ALIGN_RIGHT
            return celda
        }

        fun oCeldaSubTitulo(titulo:String, colspan: Int = 1):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fontNegrita)))
            celda.colspan = colspan
            celda.paddingLeft = 2.5f
            celda.paddingRight = 2.5f
            celda.paddingTop = 2.5f
            celda.paddingBottom = 4f

            celda.verticalAlignment = PdfPCell.ALIGN_MIDDLE
            celda.horizontalAlignment = PdfPCell.ALIGN_CENTER
            return celda
        }

        fun oCeldaDescripcionDetalle(titulo:String, colspan: Int = 1):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fontNormal)))
            celda.colspan = colspan
            celda.paddingTop = 2.5f
            celda.verticalAlignment = PdfPCell.ALIGN_CENTER
            celda.horizontalAlignment = PdfPCell.ALIGN_RIGHT
            return celda
        }

        fun oCeldaDescripcionDetalleCenter(titulo:String, colspan: Int = 1):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fontNormal)))
            celda.colspan = colspan
            celda.paddingTop = 4.5f
            celda.paddingBottom = 4.5f
            celda.paddingRight = 2.5f
            celda.paddingLeft = 2.5f
            celda.verticalAlignment = PdfPCell.ALIGN_MIDDLE
            celda.horizontalAlignment = PdfPCell.ALIGN_CENTER
            return celda
        }

        fun oCeldaDescripcionDetalleLetraEstilo(titulo:String, colspan: Int = 1, fuente: Font):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fuente)))
            celda.colspan = colspan
            celda.paddingTop = 4.5f
            celda.paddingBottom = 4.5f
            celda.paddingRight = 2.5f
            celda.paddingLeft = 2.5f
            celda.verticalAlignment = PdfPCell.ALIGN_MIDDLE
            celda.horizontalAlignment = PdfPCell.ALIGN_CENTER
            return celda
        }

        fun oCeldaDescripcionDetalleLetraCustom(titulo:String, colspan: Int = 1, fuente: Font, align: Int = ALIGN_MIDDLE):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fuente)))
            celda.colspan = colspan
            celda.paddingTop = 4.5f
            celda.paddingBottom = 4.5f
            celda.paddingRight = 2.5f
            celda.paddingLeft = 2.5f
            celda.verticalAlignment = PdfPCell.ALIGN_MIDDLE
            celda.horizontalAlignment = align
            return celda
        }

        fun oCeldaTituloLetraEstilo(titulo:String, colspan: Int = 1, fuente: Font):PdfPCell{
            val celda = PdfPCell(Phrase(Chunk(titulo, fuente)))
            celda.colspan = colspan
            celda.paddingTop = 4.5f
            celda.paddingBottom = 4.5f
            celda.paddingRight = 2.5f
            celda.paddingLeft = 2.5f
            celda.verticalAlignment = PdfPCell.ALIGN_MIDDLE
            celda.horizontalAlignment = PdfPCell.ALIGN_CENTER
            return celda
        }
    }


}