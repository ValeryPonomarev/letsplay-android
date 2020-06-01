package com.easysales.letsplay.presentation.core

import com.easysales.letsplay.R
import com.easysales.letsplay.data.exception.AppException
import com.easysales.letsplay.data.exception.NoNetworkException
import com.easysales.letsplay.data.exception.NotAuthorizedException
import com.easysales.letsplay.data.exception.ValidationException
import com.easysales.letsplay.utils.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T : BaseView> {
    private lateinit var view : T
    private val disposables = CompositeDisposable()

    open fun start(view: T) {
        LogUtils.d(LogUtils.LOG_UI,  "${this.javaClass.simpleName} start")
        this.view = view
    }

    open fun stop() {
        LogUtils.d(LogUtils.LOG_UI,  "${this.javaClass.simpleName} stop")
        this.disposables.clear()
    }

    fun getView() :T {
        return this.view
    }

    protected fun addDisposable(d: Disposable) {
        this.disposables.addAll(d)
    }

    protected open fun processException(throwable: Throwable) {
        LogUtils.e(this::class.java.simpleName, throwable.message ?: "", throwable)

        if(throwable is NotAuthorizedException) {
            return
        }

        if(throwable is ValidationException) {
            getView().showToast(throwable)
            return
        }

        if(throwable is NoNetworkException) {
            getView().showToast(R.string.error_no_network)
            return
        }

        if(throwable is AppException) {
            getView().showToast(throwable)
            return
        }

        if(throwable.message != null) {
            getView().showToast(throwable.message!!)
        }
    }
}