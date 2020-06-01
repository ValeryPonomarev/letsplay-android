package com.easysales.letsplay.data.storage

import android.content.Context
import com.easysales.letsplay.utils.LogUtils
import java.io.Serializable

class PreferencesLocalStorage(private val context: Context) : IPreferencesLocalStorage {

    override fun getString(key: String): String? {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        return sp.getString(key, null)
    }

    override fun putString(key: String, value: String?) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        sp.edit().putString(key, value).apply()
        LogUtils.d(LogUtils.LOG_DATA, "$key: $value")
    }

    override fun putBoolean(key: String, value: Boolean) {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        sp.edit().putBoolean(key, value).apply()
        LogUtils.d(LogUtils.LOG_DATA, "$key: $value")
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        val sp = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        return sp.getBoolean(key, default)
    }

    companion object KEYS {
        const val TOKEN = "TOKEN"
    }
}

data class AuthInfo(
    val token: String
): Serializable