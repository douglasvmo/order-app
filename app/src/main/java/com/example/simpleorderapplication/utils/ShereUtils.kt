package com.example.simpleorderapplication.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object ShereUtils {
    fun sherePdfWithWhatsapp(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val shereIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(shereIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Whatsapp não instalado", Toast.LENGTH_SHORT).show()
        }
    }
}