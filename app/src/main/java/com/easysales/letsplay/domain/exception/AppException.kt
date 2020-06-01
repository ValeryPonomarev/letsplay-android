package com.easysales.letsplay.data.exception

import android.content.Context
import androidx.annotation.StringRes
import java.lang.RuntimeException

open class AppException() : RuntimeException() {
    @StringRes
    var messageRes: Int? = null
        private set

    var params: List<String> = ArrayList()
        private set

    constructor(@StringRes messageRes: Int): this() {
        this.messageRes = messageRes
    }

    constructor(@StringRes messageRes: Int, vararg params: String): this(messageRes) {
        this.params = params.asList()
    }

    fun getAppMessage(context: Context): String {
        if(messageRes != null) {
            return context.getString(messageRes!!, params)
        } else {
            return message ?: ""
        }
    }
}
