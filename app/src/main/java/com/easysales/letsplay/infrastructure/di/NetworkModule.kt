package com.easysales.letsplay.infrastructure.di

import android.content.Context
import com.easysales.letsplay.data.exception.ApiException
import com.easysales.letsplay.data.exception.NoNetworkException
import com.easysales.letsplay.data.fake.FakeHttpClient
import com.easysales.letsplay.data.http.ApiError
import com.easysales.letsplay.data.http.ApiResponse
import com.easysales.letsplay.data.http.HttpClient
import com.easysales.letsplay.utils.LogUtils
import com.easysales.letsplay.utils.ToggleFeature
import com.easysales.letsplay.utils.android.isNetworkAvailable
import com.easysales.letsplay.utils.getApiDateTimeFormat
import com.google.gson.*
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.security.cert.CertificateException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
open class NetworkModule(private val API_URL: String) {
    private val timeout: Long = 60000

    @Provides
    @Singleton
    open fun provideHttpClient(retrofit: Retrofit) : HttpClient  {
        if (ToggleFeature.FAKE_API) {
            return FakeHttpClient()
        }

        return retrofit.create(HttpClient::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        callAdapterFactory: CallAdapter.Factory
    ) : Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(httpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build()
        retrofit.create(HttpClient::class.java)

        return retrofit
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named(NAMES.LOG_INTERCEPTOR) logInterceptor: Interceptor,
        @Named(NAMES.ERROR_INTERCEPTOR) errorInterceptor: Interceptor,
        @Named(NAMES.CONNECTION_INTERCEPTOR) connectionInterceptor: Interceptor,
        sslSocketFactory: SSLSocketFactory?,
        trustManagers: Array<TrustManager>
    ): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .addInterceptor(connectionInterceptor)
            .addInterceptor(logInterceptor)
            .addInterceptor(errorInterceptor)
            .connectTimeout(timeout, TimeUnit.MILLISECONDS)
            .readTimeout(timeout, TimeUnit.MILLISECONDS)
            .writeTimeout(timeout, TimeUnit.MILLISECONDS)

        if(sslSocketFactory != null && trustManagers.isNotEmpty()) {
            builder.sslSocketFactory(sslSocketFactory, trustManagers[0] as X509TrustManager)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    @Provides @Singleton
    fun provideGson(): Gson {
        val dateSerializer = JsonSerializer<Date> { date, type, context ->
            JsonPrimitive(SimpleDateFormat(getApiDateTimeFormat(), Locale.getDefault()).format(date))
        }

        val dateDeserializer = JsonDeserializer<Date> {json, typeOfT, context ->
            try {
                return@JsonDeserializer SimpleDateFormat(getApiDateTimeFormat(), Locale.getDefault()).parse(json.asString)
            } catch ( e: ParseException ) {
                return@JsonDeserializer null
            }
        }

        return GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .registerTypeAdapter(Date::class.java, dateSerializer)
            .registerTypeAdapter(Date::class.java, dateDeserializer)
            .create()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Named(NAMES.ERROR_INTERCEPTOR)
    @Singleton
    fun provideErrorInterceptor(): Interceptor {
        fun getApiError(apiResponse: ApiResponse<Any>): ApiError {
            if(apiResponse.errors.isEmpty()) {
               return ApiError("Unknown", "")
            } else {
                return apiResponse.errors[0];
            }
        }

        return object : Interceptor {
            val gson = Gson()

            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val response = chain.proceed(request)

                if(response.code != 200) {
                    var apiError = ApiError("Unknown error [${response.code}]")
                    throw ApiException(apiError)
                }

                var responseString = response.body!!.string()
                val apiResponse = gson.fromJson(responseString, ApiResponse::class.java)
                if(!apiResponse.status) {
                    if(apiResponse.errors.isNotEmpty()) {
                       throw ApiException(apiResponse.errors[0])
                    } else {
                        throw ApiException("Unknown error")
                    }
                }

                responseString = gson.toJson(apiResponse.data).toString()

                return response.newBuilder()
                    .body(responseString.toResponseBody(response.body!!.contentType())).build()
            }
        }
    }

    @Provides
    @Named(NAMES.LOG_INTERCEPTOR)
    @Singleton
    fun provideLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor {
            LogUtils.dFile(LogUtils.LOG_NET, it)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Named(NAMES.CONNECTION_INTERCEPTOR)
    @Singleton
    fun provideConnectionInterceptor(context: Context): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                if(!isNetworkAvailable(context)) {
                    throw NoNetworkException()
                }

                return chain.proceed(request)
            }
        }
    }

    @Provides @Singleton fun provideSslSocketFactory(trustManagers: Array<TrustManager>): SSLSocketFactory? {
        return try {
            val trustAllSslContext = SSLContext.getInstance("SSL")
            trustAllSslContext.init(null, trustManagers, java.security.SecureRandom())
            trustAllSslContext.socketFactory
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Provides @Singleton fun provideTrustManagers(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })
    }

    @Provides @Singleton fun provideParseStrategy(): ParseStrategy = ParseStrategy.DERESTRICT

    private object NAMES {
        const val LOG_INTERCEPTOR = "LOG_INTERCEPTOR"
        const val ERROR_INTERCEPTOR = "ERROR_INTERCEPTOR"
        const val CONNECTION_INTERCEPTOR = "CONNECTION_INTERCEPTOR"
    }

    enum class ParseStrategy {
        RESTRICT, DERESTRICT
    }
}