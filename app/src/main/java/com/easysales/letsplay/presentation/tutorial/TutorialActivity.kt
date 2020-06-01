package com.easysales.letsplay.presentation.tutorial

import android.os.Bundle
import android.view.View
import com.easysales.letsplay.R
import com.easysales.letsplay.infrastructure.App
import com.easysales.letsplay.presentation.core.BaseActivity

class TutorialActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_base

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        App.getDI().addTutorialComponent()
        findViewById<View>(R.id.toolbar).visibility = View.GONE
        showFragment(TutorialFragment.newInstance(false), true)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isFinishing) {
            App.getDI().clearTutorialComponent()
        }
    }
}
