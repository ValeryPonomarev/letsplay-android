package com.easysales.letsplay.data.exception

import androidx.annotation.StringRes
import com.easysales.letsplay.R

class OperationException(@StringRes messageRes: Int, vararg params: String) : AppException(messageRes, *params) {
    constructor() : this(R.string.error_business_operation)
}
