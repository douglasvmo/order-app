package com.example.simpleorderapplication.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.simpleorderapplication.domain.Order

import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.graphics.withRotation


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

        paint.textSize = 32f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.withRotation(-90f, 20f, y) {
            drawText("M", 20f, y +20, paint)
        }
        paint.textSize = 18f
        canvas.drawText("A", 20f, y, paint)
        canvas.drawText("R", 30f, y +20, paint)

        paint.typeface = Typeface.DEFAULT

        // right text block
        var rightMargin = 570f
        paint.textSize = 18f;
        var text  = "Arte Requinte Moveis"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)
        paint.textSize = 14f
        y += 25f
        text = "Rua Das Margaridas, 1085"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)
        y += 25f
        text = "Guaruja - Cascavel"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)
        y += 25f
        text = "PR - CEP 8580-480"
        canvas.drawText(text, rightMargin - paint.measureText(text), y, paint)

        y+= 40f
        paint.textSize = 18f
        text = "Pedido N ${order.id}"
        canvas.drawText(text, (width.div(2) - paint.measureText(text)).div(2),y, paint)

        y += 25f
        paint.textSize = 14f
        canvas.drawText("Cliente: ${order.clientName}", 20f, y, paint)
        y += 25f
        val formater = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        canvas.drawText("Data: ${formater.format(order.date)}", 20f, y, paint)
        y += 40f

        // Cabeçalhos da tabela
        val colQtd = 40f
        val colDesc = 100f
        val colPreco = 380f
        val colSubtotal = 480f
        val rowHeight = 25f
        val colEnd = 570f

        // Desenha o cabeçalho
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawLine(colQtd, y, colEnd, y, paint) // linha superior
        y += rowHeight

        canvas.drawText("Quant.", colQtd + 4f, y - 8f, paint)
        canvas.drawText("Descrição", colDesc + 4f, y - 8f, paint)
        canvas.drawText("Preço", colPreco + 4f, y - 8f, paint)
        canvas.drawText("Subtotal", colSubtotal + 4f, y - 8f, paint)
        y += 5f
        canvas.drawLine(colQtd, y, colEnd, y, paint) // linha após o cabeçalho
        paint.typeface = Typeface.DEFAULT


        order.products.forEach {
            y += rowHeight

            val subtotal = it.quantity * it.price
            canvas.drawText("%02d".format(it.quantity), colQtd + 4f, y - 8f, paint)
            canvas.drawText(it.description, colDesc + 4f, y - 8f, paint)
            canvas.drawText("R$ %.2f".format(it.price.div(100.0)), colPreco + 4f, y - 8f, paint)
            canvas.drawText("R$ %.2f".format(subtotal.div(100.0)), colSubtotal + 4f, y - 8f, paint)

            canvas.drawLine(colQtd, y, colEnd, y, paint) // linha inferior da linha atual

        }
        y += rowHeight
        text = "Total"
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Total", colSubtotal -4f -paint.measureText(text),y -8f, paint)
        canvas.drawText("R$ %.2f".format(order.amount.div(100.0)),colSubtotal +4f, y -8f, paint)
        paint.typeface = Typeface.DEFAULT_BOLD

        // the last horizontal line
        canvas.drawLine(colSubtotal, y, colEnd, y, paint)

        // Linhas verticais da tabela
        val startY = y - ((order.products.size + 1) * rowHeight)
        val endY = y
        canvas.drawLine(colQtd, startY, colQtd, endY - rowHeight, paint)
        canvas.drawLine(colDesc, startY, colDesc, endY - rowHeight, paint)
        canvas.drawLine(colPreco, startY, colPreco - rowHeight, endY, paint)
        canvas.drawLine(colSubtotal, startY, colSubtotal, endY, paint)
        canvas.drawLine(colEnd, startY, colEnd, endY, paint)

        pdfDocument.finishPage(page)

        val file = File(context.cacheDir, "pedido_${order.id}.pdf")
        file.outputStream().use {
            pdfDocument.writeTo(it)
        }

        pdfDocument.close()
        return file
    }

}