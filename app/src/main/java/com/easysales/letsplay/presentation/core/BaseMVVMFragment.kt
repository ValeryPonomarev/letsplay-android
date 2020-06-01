package com.easysales.letsplay.presentation.core

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseMVVMFragment<VM : BaseVM> : BaseFragment() {
    private val compositeDisposable = CompositeDisposable()

    abstract fun getVM(): VM

    override fun initialize() {
        addDisposable(getVM().getMessages().retry().subscribe(
            this::showMessage,
            this::handleException
        ))

        addDisposable(getVM().getLoadStates().retry().subscribe(
            {
                if(it.isLoading) {
                    showProgress()
                } else {
                    hideProgress()
                }
            },
            this::handleException
        ))
    }

    protected fun showMessage(messageDto: MessageDto) {
        showToast(messageDto.description, *messageDto.params.toTypedArray())
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun handleException(t: Throwable) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}