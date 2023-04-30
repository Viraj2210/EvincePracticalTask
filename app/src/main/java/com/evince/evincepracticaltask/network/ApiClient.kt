package com.d2k.shg.networking

import com.evince.evincepracticaltask.BuildConfig
import com.evince.evincepracticaltask.network.IApiService
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


object ApiClient {

    const val BASE_URL = "https://reqres.in/api/"

        var TOKEN: String? = null
        val aPIService: IApiService
            get() {


            val retrofit = Retrofit.Builder()
            retrofit
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(customLogInterceptor())
                .build()
            return retrofit.build().create(IApiService::class.java)
        }

    fun customLogInterceptor(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor(CustomHttpLogging())
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .connectionSpecs(
                    Arrays.asList(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.CLEARTEXT
                    )
                )
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient.Builder()
                .connectionSpecs(
                    Arrays.asList(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.CLEARTEXT
                    )
                )
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .build()
        }
    }
}
