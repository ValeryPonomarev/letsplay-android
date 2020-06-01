package com.easysales.letsplay.data.storage

class TestPreferencesLocalStorage: IPreferencesLocalStorage {
    val data: MutableMap<String, Any> = java.util.HashMap()

    override fun getString(key: String): String? {
        return data[key] as String?
    }

    override fun putString(key: String, value: String?) {
        if(value == null) {
            data.remove(key)
        } else {
            data[key] = value
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        data[key] = value
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        var result = data.get(key) as Boolean?
        if(result == null) {
            result = default;
        }

        return result
    }
}