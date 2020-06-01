package com.easysales.letsplay.data

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.easysales.letsplay.infrastructure.di.NetworkModule
import com.easysales.letsplay.data.http.ApiMapper
import com.easysales.letsplay.data.http.HttpClient
import com.easysales.letsplay.utils.LogUtils
import java.lang.Exception

open class BaseRepository(
    private val httpClient: HttpClient,
    protected val schedulerProvider: SchedulerProvider,
    protected val gson: Gson
) {
    private val strategy = NetworkModule.ParseStrategy.DERESTRICT
    private val warningsSubject: PublishSubject<Warning> = PublishSubject.create()
    protected val apiMapper = ApiMapper(gson)
    public val warningsObservable: Observable<Warning> = warningsSubject

    protected fun getHttpClient(): HttpClient {
        return httpClient
    }

    protected fun postWarning(warning: Warning) {
        warningsSubject.onNext(warning)
    }

    protected fun <TO, FROM: Any> getItems(response: List<TO>, action: (TO) -> FROM) : List<FROM>  {
        return response.mapNotNull { getOrThrow { action(it) } }
    }

    private fun <T> getOrThrow(action: () -> T): T? {
        try {
            return action.invoke()
        } catch (exception: Exception) {
            if(strategy == NetworkModule.ParseStrategy.RESTRICT) throw exception
            else {
                LogUtils.eFile(LogUtils.LOG_DATA, "parsing api response failed: ${exception.message}", exception)
                postWarning(object : Warning {})
                return null
            }
        }
    }

    interface Warning {

    }
}