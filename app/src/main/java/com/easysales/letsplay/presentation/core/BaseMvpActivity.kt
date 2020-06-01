package com.easysales.letsplay.presentation.core

import android.os.Bundle

abstract class BaseMvpActivity<P : BasePresenter<V>, V : BaseView> : BaseActivity() {

    abstract fun getPresenter() : P

    override fun initActivity(savedInstantState: Bundle?) {
        getPresenter().start(getBaseView())
    }

    override fun onDestroy() {
        super.onDestroy()
        getPresenter().stop()
    }

    protected fun getBaseView() = this as V
}