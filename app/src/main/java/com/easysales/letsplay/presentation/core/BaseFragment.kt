package com.easysales.letsplay.presentation.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.easysales.letsplay.data.exception.AppException

abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    @LayoutRes
    abstract fun getLayoutId() : Int

    protected open fun initialize() {
    }


    protected fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }

    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes msg: Int) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes msg: Int, vararg params: String) {
        Toast.makeText(context, getString(msg, *params), Toast.LENGTH_SHORT).show()
    }

    fun showToast(throwable: AppException) {
        if(throwable.messageRes != null) {
            showToast(throwable.messageRes!!, *throwable.params.toTypedArray())
        } else {
            showToast(throwable.message!!)
        }
    }

    fun showProgress() {
        getBaseActivity().showProgress()
    }

    fun hideProgress() {
        getBaseActivity().hideProgress()
    }

    fun showErrorContainer() {
        getBaseActivity().showErrorContainer()
    }
}