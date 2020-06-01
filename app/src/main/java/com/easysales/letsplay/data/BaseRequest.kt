package com.easysales.letsplay.data

import com.google.gson.Gson

open class BaseRequest(
    @Transient private val gson: Gson
) {
    override fun toString(): String {
        return toJson()
    }

    fun toJson(): String {
        return gson.toJson(this)
    }
}

abstract class BaseArrayRequest(@Transient private val gson: Gson) {
    abstract val data: List<Any>

    override fun toString(): String {
        return toJson()
    }

    fun toJson(): String {
        return gson.toJson(data)
    }
}