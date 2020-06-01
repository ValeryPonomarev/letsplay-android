package com.easysales.letsplay.presentation.core

abstract class BaseMvpFragment<P : BasePresenter<V>, V : BaseView> : BaseFragment() {

    abstract fun getPresenter() : P

    override fun initialize() {
        getPresenter().start(getBaseView())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        getPresenter().stop()
    }

    protected fun getBaseView() = this as V
}