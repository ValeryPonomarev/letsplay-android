package com.easysales.letsplay.utils

import java.text.SimpleDateFormat
import java.util.*

fun getApiDateTimeFormat() = "yyyy-MM-dd HH:mm:ss"
fun asApiDateTime(dateString: String): Date = SimpleDateFormat(getApiDateTimeFormat(), Locale.getDefault()).parse(dateString)