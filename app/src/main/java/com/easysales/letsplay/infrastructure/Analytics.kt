package com.easysales.letsplay.infrastructure

import android.os.Bundle
import com.easysales.letsplay.utils.AnalyticUtils


object Analytics {
    data class LoginEvent(val userId: String) : AnalyticUtils.Event {
        override val name = "Login"
        override val params: Bundle? = null
    }
    class LogoutEvent(): AnalyticUtils.Event {
        override val name = "LogoutEvent"
        override val params: Bundle? = null
    }
    class OrderAccept(): AnalyticUtils.Event {
        override val name = "OrderAccept"
        override val params: Bundle? = null
    }
    class OrderRefuse(reason: String): AnalyticUtils.Event {
        override val name = "OrderRefuse"
        override val params: Bundle?

        init {
            params = Bundle()
            params.putString(AnalyticUtils.NAME, reason)
        }
    }
    class OrderProductAccept(): AnalyticUtils.Event {
        override val name = "OrderProductAccept"
        override val params: Bundle? = null
    }
    class OrderProductRefuse(reason: String): AnalyticUtils.Event {
        override val name = "OrderProductRefuse"
        override val params: Bundle?

        init {
            params = Bundle()
            params.putString(AnalyticUtils.NAME, reason)
        }
    }
    class RefundAccept(): AnalyticUtils.Event {
        override val name = "RefundAccept"
        override val params: Bundle? = null
    }
    class RefundRefuse(reason: String): AnalyticUtils.Event {

        override val name = "RefundRefuse"
        override val params: Bundle?

        init {
            params = Bundle()
            params.putString(AnalyticUtils.NAME, reason)
        }

    }
    class RefundProductAccept(): AnalyticUtils.Event {
        override val name = "RefundProductAccept"
        override val params: Bundle? = null
    }
    class RefundProductRefuse(reason: String): AnalyticUtils.Event {
        override val name = "RefundProductRefuse"
        override val params: Bundle?

        init {
            params = Bundle()
            params.putString(AnalyticUtils.NAME, reason)
        }
    }
    class PointArrive(): AnalyticUtils.Event {
        override val name = "PointArrive"
        override val params: Bundle? = null
    }
    class PointLeft(): AnalyticUtils.Event {
        override val name = "PointLeft"
        override val params: Bundle? = null
    }
    class StartWorkDay(): AnalyticUtils.Event {
        override val name = "StartWorkDay"
        override val params: Bundle? = null
    }
    class EndWorkDay(): AnalyticUtils.Event {
        override val name = "EndWorkDay"
        override val params: Bundle? = null
    }
}