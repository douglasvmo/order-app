package com.example.simpleorderapplication.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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
        text = "Pedido Nº ${order.id}"
        canvas.drawText(text, (width - paint.measureText(text)).div(2),y, paint)

        y += 25f
        paint.textSize = 14f
        canvas.drawText("Cliente: ${order.clientName}", 25f, y, paint)
        y += 25f
        canvas.drawText("Data: ${Formater.dateToString(order.date)}", 25f, y, paint)
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
        y += rowHeight

        canvas.drawText("Quant.", colQtd + 4f, y - 8f, paint)
        canvas.drawText("Descrição", colDesc + 4f, y - 8f, paint)
        canvas.drawText("Unitario", colPreco + 4f, y - 8f, paint)
        canvas.drawText("Subtotal", colSubtotal + 4f, y - 8f, paint)

        canvas.drawLine(colQtd, y, colEnd, y, paint) // linha após o cabeçalho
        paint.typeface = Typeface.DEFAULT


        order.products.forEach {
            y += rowHeight

            val subtotal = it.quantity * it.price
            text = "%02d".format(it.quantity)
            canvas.drawText(text, (colQtd + colDesc - paint.measureText(text)).div(2), y - 8f, paint)
            val newY = drawMultilineText(canvas, it.description, colDesc +4f, y - 8f, colPreco - colDesc - 8f, rowHeight, paint)
            y = newY + 8f
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
        val startY = y - ((order.products.size +2) * rowHeight)
        val endY = y
        canvas.drawLine(colQtd, startY, colQtd, endY -rowHeight, paint)
        canvas.drawLine(colDesc, startY, colDesc, endY -rowHeight, paint)
        canvas.drawLine(colPreco, startY, colPreco, endY -rowHeight, paint)
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

    private fun drawMultilineText(
        canvas: Canvas,
        text: String,
        startX: Float,
        startY: Float,
        maxWidth: Float,
        lineHeight: Float,
        paint: Paint
    ): Float {
        var y = startY
        var textLeft = text

        while (textLeft.isNotEmpty()) {
            val charsFit = paint.breakText(textLeft, true, maxWidth, null)
            val line = textLeft.substring(0, charsFit)
            canvas.drawText(line, startX, y, paint)

            y += lineHeight
            textLeft = textLeft.substring(charsFit)
        }

        return y

    }

}