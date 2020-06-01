package com.easysales.letsplay.presentation.core

import androidx.lifecycle.ViewModel
import com.easysales.letsplay.R
import com.easysales.letsplay.data.exception.AppException
import com.easysales.letsplay.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseVM : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val messages = PublishSubject.create<MessageDto>()
    private val loadState = BehaviorSubject.create<LoaderState>()

    public abstract fun start()

    public fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
    public fun getMessages(): Observable<MessageDto> = this.messages
    public fun getLoadStates(): Observable<LoaderState> = this.loadState

    protected fun showMessage(message: MessageDto) {
        messages.onNext(message);
    }

    protected fun showLoader() {
        loadState.onNext(LoaderState(true))
    }

    protected fun hideLoader() {
        loadState.onNext(LoaderState(false))
    }

    protected fun handleException(e: Throwable) {
        LogUtils.e(LogUtils.LOG_UI, e.message ?: "", e)
        if(e is AppException) {
            this.messages.onNext(MessageDto(R.string.error_business, e.messageRes ?: R.string.error_business, MessageType.ERROR, e.params))
        } else {
            this.messages.onNext(MessageDto(R.string.error, R.string.error_unknown, MessageType.ERROR, listOf(e.message ?: "")))
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}