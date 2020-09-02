package com.dinokeylas.jastipinaja.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        @JvmStatic
        fun getStringFormatedDate(date: Date): String{
            return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        }
    }
}