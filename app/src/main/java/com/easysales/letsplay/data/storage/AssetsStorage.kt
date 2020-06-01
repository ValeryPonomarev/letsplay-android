package com.easysales.letsplay.data.storage

import android.content.Context
import com.easysales.letsplay.R
import com.easysales.letsplay.data.exception.AppException
import java.io.InputStream

class AssetsStorage(private val context: Context) {
    fun readTextFromFile(path: String) : String {
        try {
            val stream: InputStream = context.assets.open(path)
            val size: Int = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            return String(buffer)
        } catch (t: Throwable) {
            throw AppException(R.string.error_read_file);
        }
    }
}