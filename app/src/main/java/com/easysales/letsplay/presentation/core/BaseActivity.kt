package com.easysales.letsplay.presentation.core

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.easysales.letsplay.R
import com.easysales.letsplay.data.exception.AppException
import com.easysales.letsplay.presentation.OptionsDialog

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initActivity(savedInstanceState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    @LayoutRes
    abstract fun getLayoutId() : Int

    protected open fun initActivity(savedInstantState: Bundle?) {

    }

    protected fun showFragment(fragment: Fragment, addToBack: Boolean = false, containerId: Int = R.id.container) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE)
        transaction.replace(containerId, fragment, fragment.javaClass.simpleName)
        if(addToBack) transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
        hideKeyboard()
        hideErrorContainer()
    }

    fun goBack() {
        hideKeyboard()
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
            if(supportFragmentManager.backStackEntryCount == 0) {
                finish()
            }
        }
    }

    fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun showProgress() {
        hideKeyboard()
        val progressBar: View = findViewById(R.id.progressBar)
        val container: View = findViewById(R.id.container)

        progressBar.visibility = View.VISIBLE
        container.visibility = View.GONE
    }

    fun hideProgress() {
        val progressBar: View = findViewById(R.id.progressBar)
        val container: View = findViewById(R.id.container)

        progressBar.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    fun showErrorContainer() {
        val errorContainer: View = findViewById(R.id.containerError)
        val container: View = findViewById(R.id.container)

        errorContainer.visibility = View.VISIBLE
        container.visibility = View.GONE
    }

    fun hideErrorContainer() {
        val errorContainer: View = findViewById(R.id.containerError)
        val container: View = findViewById(R.id.container)

        errorContainer.visibility = View.GONE
        container.visibility = View.VISIBLE
    }

    fun setToolbarTitle(title: String) {
        val tv = findViewById<TextView>(R.id.toolbar_title)
        if(tv != null) tv.text = title
    }

    fun setToolbarTitle(@StringRes title: Int) {
        val tv = findViewById<TextView>(R.id.toolbar_title)
        if(tv != null) tv.setText(title)
    }

    fun showDialog(params: OptionsDialog.OptionsDialogParams) : OptionsDialog {
        val dialog = OptionsDialog.newInstance(params)
        dialog.show(supportFragmentManager, "OPTIONS_DIALOG")
        return dialog
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes msg: Int) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes msg: Int, vararg params: String) {
        Toast.makeText(this, getString(msg, *params), Toast.LENGTH_SHORT).show()
    }

    fun showToast(throwable: AppException) {
        if(throwable.messageRes != null) {
            showToast(throwable.messageRes!!, *throwable.params.toTypedArray())
        } else {
            showToast(throwable.message!!)
        }
    }
}