package com.easysales.letsplay.infrastructure

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.easysales.letsplay.infrastructure.di.ComponentManager
import com.easysales.letsplay.utils.LogUtils
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*


class App : Application() {
    private val TAG = "App"

    override fun onCreate() {
        super.onCreate()
        componentManager = ComponentManager(this)
//        setUncaughtExceptionHandler()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                LogUtils.i(LogUtils.LOG_UI, String.format("view.created=[%s]", activity.javaClass.simpleName), TAG)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                LogUtils.i(LogUtils.LOG_UI, String.format("view.stopped=[%s]", activity.javaClass.simpleName), TAG)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun setUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Crashlytics.logException(throwable)
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            throwable.printStackTrace(pw)

            LogUtils.eFile(LogUtils.LOG_APP, "FATAL EXCEPTION [${Date()}]", throwable)
            try {
                Thread.sleep(4000)
            } catch (e: InterruptedException) { }
            System.exit(2)
        }
    }

    companion object {
        private lateinit var componentManager: ComponentManager

        fun getDI() : ComponentManager {
            return componentManager;
        }
    }
}