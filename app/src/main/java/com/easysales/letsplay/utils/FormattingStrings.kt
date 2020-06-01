package com.easysales.letsplay.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object FormatPatterns {
    const val DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss"
    const val TIME_FORMAT = "HH:mm"
    const val FRACTIONAL_NUMBER_FORMAT = "#.##"
}


fun Double.toUIFormat(): String = DecimalFormat(FormatPatterns.FRACTIONAL_NUMBER_FORMAT).format(this)
fun Date.toStringFormat(pattern: String) = SimpleDateFormat(pattern).format(this)
fun Date.toUITimeFormat(): String = this.toStringFormat(FormatPatterns.TIME_FORMAT)

fun toDoubleLetterIndex(index: Int): String {
    assert(index < 100)
    return if(index < 10) "0$index"
    else index.toString()
}
fun toTimeCountdounFormat(millis: Long): String {
    return SimpleDateFormat("mm:ss").format(Date(millis))
}
fun toRDExtendedFormat(date: Date): String {
    val cal = Calendar.getInstance()
    cal.time = date
    return cal.get(Calendar.DAY_OF_MONTH).toString() + " " + MONTH_RD[cal.get(Calendar.MONTH)].toLowerCase() + " " + cal.get(
        Calendar.YEAR
    )
}
fun toWeekNumber(date: Date) : String {
    val cal = Calendar.getInstance()
    cal.time = date
    return cal.get(Calendar.WEEK_OF_YEAR).toString()
}
val MONTH_RD = arrayOf(
    "Января",
    "Февраля",
    "Марта",
    "Апреля",
    "Мая",
    "Июня",
    "Июля",
    "Августа",
    "Сентября",
    "Октября",
    "Ноября",
    "Декабря"
)