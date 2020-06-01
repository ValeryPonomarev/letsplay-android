package com.easysales.letsplay.data.http

import com.google.gson.annotations.SerializedName

open class ApiResponse<DATA> {
    @SerializedName("status")
    var status: Boolean = false

    @SerializedName("errors")
    lateinit var errors: List<ApiError>

    @SerializedName("data")
    var data: DATA? = null
}

data class ApiError(
    @SerializedName("text")
    var text: String,
    @SerializedName("code")
    var code: String = CODE_UNKNOWN
) {
    companion object {
        public val CODE_UNKNOWN = "unknown"
    }
}