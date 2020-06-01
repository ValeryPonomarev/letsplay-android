package com.easysales.letsplay.presentation.core

import androidx.annotation.StringRes
import com.easysales.letsplay.data.exception.AppException

interface BaseView {
    fun showToast(msg: String)
    fun showToast(@StringRes msg: Int)
    fun showToast(@StringRes msg: Int, vararg params: String)
    fun showToast(throwable: AppException)
    fun showProgress()
    fun hideProgress()
    fun showErrorContainer()
}