package com.easysales.letsplay.data.exception

import androidx.annotation.StringRes
import com.easysales.letsplay.R

class NotFoundException(@StringRes messageRes: Int, vararg params: String) : AppException(messageRes, *params) {
}
