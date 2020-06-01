package com.easysales.letsplay.utils

import android.content.Context
import android.os.Bundle
//import com.google.firebase.analytics.FirebaseAnalytics

interface IAnalyticUtils {
    fun event(event: AnalyticUtils.Event)
}

class AnalyticUtils(private val context: Context) : IAnalyticUtils {

//    @SuppressLint("MissingPermission")
//    private val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    interface Event {
        val name: String
        val params: Bundle?
    }

    override fun event(event: Event) {
//        analytics.logEvent(event.name, event.params)
    }

    companion object PARAM_NAMES {
        const val NAME = "FirebaseAnalytics.Param.ITEM_NAME"
    }
}
