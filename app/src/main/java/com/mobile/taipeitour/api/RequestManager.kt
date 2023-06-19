package com.mobile.taipeitour.api

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

open class RequestManager {
    protected var mUrl: String? = null
    constructor(url: String) {
        mUrl = url
    }

    open fun getRetrofit(context: Context?): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            var request = original.newBuilder()
                .header("Accept", "application/json")
                .method(original.method, original.body)
                .build()

            return@addInterceptor chain.proceed(request)
        }

        val client: OkHttpClient = httpClient.build()
        return Retrofit
            .Builder()
            .baseUrl(mUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}