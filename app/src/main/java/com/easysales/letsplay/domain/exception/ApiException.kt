package com.easysales.letsplay.data.exception

import com.easysales.letsplay.data.http.ApiError
import java.lang.RuntimeException

open class ApiException : RuntimeException {
    private var code: Int? = null
        get() = field

    private var errorMessage: String? = null
        get() = field

    constructor(apiError: ApiError): this(apiError.text)

    constructor(message: String, code: Int = UNKNOWN) : super(message) {
        this.errorMessage = message
        this.code = code
    }

    constructor()

    companion object CODES {
        const val UNKNOWN: Int = 0
        const val NO_NETWORK: Int = 100
        const val SESSION_NOT_FOUND: Int = 100
    }
}