package com.example.simpleorderapplication.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formater {
    fun dateToString(date: Date): String  {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}