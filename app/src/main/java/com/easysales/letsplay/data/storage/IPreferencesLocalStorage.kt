package com.easysales.letsplay.data.storage

interface IPreferencesLocalStorage {
    fun getString(key: String): String?
    fun putString(key: String, value: String?)
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, default: Boolean): Boolean
}