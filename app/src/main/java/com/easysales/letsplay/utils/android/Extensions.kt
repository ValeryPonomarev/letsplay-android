package com.easysales.letsplay.utils.android

import android.content.Intent


fun Intent.getIntOrThrow(argName: String): Int {
    val value = this.getIntExtra(argName, -1)
    if(value == -1) throw IllegalArgumentException(argName)
    return value
}

fun Intent.getLongOrThrow(argName: String): Long {
    val value = this.getLongExtra(argName, -1L)
    if(value == -1L) throw IllegalArgumentException(argName)
    return value
}

fun Intent.getStringOrThrow(argName: String): String {
    val value = this.getStringExtra(argName)
    if(value == "") throw IllegalArgumentException(argName)
    return value
}
