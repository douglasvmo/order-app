package com.example.simpleorderapplication.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.simpleorderapplication.data.models.Order
import java.io.File


object PdfUtils {

    fun getPdfFromOrder(context: Context, order: Order ): File {
        val width = 595
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()

        var y = 40f

        //Logo

        // right text block
        var rightMargin = 570f
        paint.textSize = 20f;
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.color = Color.RED
        var text  = "Arte Requinte Moveis"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)
        y += 25f
        paint.textSize = 14f
        paint.typeface = Typeface.DEFAULT
        paint.color = Color.BLACK
        text = "Telefone (45) 99909-7071"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)
        y += 20f
        paint.textSize = 12f
        text = "Rua Das Margaridas, 1085"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)
        y += 14f
        text = "Guaruja - Cascavel"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)
        y += 14f
        text = "PR - CEP 8580-480"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)

        y+= 40f
        paint.textSize = 18f
        text = "Pedido Nº ${order.code.toString()}"
        canvas.drawText(text, (width - paint.measureText(text)).div(2),y, paint)

        y += 25f
        paint.textSize = 14f
        canvas.drawText("Cliente: ${order.clientName}", 25f, y, paint)
        y += 25f
        canvas.drawText("Data: ${order.createdAt.toFormattedDate()}", 25f, y, paint)
        y += 40f

        // Cabeçalhos da tabela
        val colQtd = 25f
        val colDesc = 75f
        val colPreco = 380f
        val colSubtotal = 480f
        val rowHeight = 25f
        val colEnd = 570f

        // Desenha o cabeçalho
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawLine(colQtd, y, colEnd, y, paint) // linha superior
        var tableYstart = y;
        y += rowHeight

        canvas.drawText("Quant.", colQtd + 4f, y - 8f, paint)
        canvas.drawText("Descrição", colDesc + 4f, y - 8f, paint)
        canvas.drawText("Unitario", colPreco + 4f, y - 8f, paint)
        canvas.drawText("Subtotal", colSubtotal + 4f, y - 8f, paint)

        canvas.drawLine(colQtd, y, colEnd, y, paint) // linha após o cabeçalho
        paint.typeface = Typeface.DEFAULT


        order.products.forEach {
            val startY = y;

            while (it.description.isNotEmpty()) {
                y += rowHeight
                val charsFit = paint.breakText(it.description, true, colPreco - colDesc -15f, null)
                val line = it.description.substring(0, charsFit)
                canvas.drawText(line, colDesc + 8f, y -8f, paint)
                it.description = it.description.substring(charsFit)
            }

            val middle = startY + (y -startY).div(2) +5f
            text = "%02d".format(it.quantity)
            canvas.drawText(text, (colQtd + colDesc - paint.measureText(text)).div(2), middle, paint)

            val price = it.priceCents.div(100.0)
            canvas.drawText(it.description, colDesc + 4f, y - 8f, paint)
            canvas.drawText("R$ %.2f".format(price), colPreco + 4f, middle, paint)

            val subtotal = it.quantity * price
            canvas.drawText("R$ %.2f".format(subtotal), colSubtotal + 4f, middle, paint)

            canvas.drawLine(colQtd, y, colEnd, y, paint) // linha inferior da linha atual

        }
        y += rowHeight
        text = "Total"
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Total", colSubtotal -4f -paint.measureText(text),y -8f, paint)
        canvas.drawText("R$ %.2f".format(order.products.sumOf { it.priceCents.div(100.0) * it.quantity }),colSubtotal +4f, y -8f, paint)
        paint.typeface = Typeface.DEFAULT_BOLD

        // the last horizontal line
        canvas.drawLine(colSubtotal, y, colEnd, y, paint)

        // Linhas verticais da tabela
        val endY = y - rowHeight
        canvas.drawLine(colQtd, tableYstart, colQtd, endY, paint)
        canvas.drawLine(colQtd, tableYstart, colQtd, endY , paint)
        canvas.drawLine(colDesc, tableYstart, colDesc, endY, paint)
        canvas.drawLine(colPreco, tableYstart, colPreco, endY, paint)
        canvas.drawLine(colSubtotal, tableYstart, colSubtotal, y, paint)
        canvas.drawLine(colEnd, tableYstart, colEnd, y, paint)

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "pedido_${order.id.toString()}.pdf")
        file.outputStream().use {
            pdfDocument.writeTo(it)
        }

        pdfDocument.close()
        return file
    }

}