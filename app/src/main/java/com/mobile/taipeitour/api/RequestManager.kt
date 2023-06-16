package com.mobile.taipeitour.api

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class RequestManager {

    private val client = OkHttpClient()

    fun requestAttractions(act: Activity, lang: String, myCallback: (result: String) -> Unit) {
        val request = Request.Builder()
            .header("Accept", "application/json")
            .get()
            .url("${ApiConstants.API_DOMAIN}/$lang/Attractions/All?page=1")
            .build()

        request(request) {
            act.runOnUiThread(Runnable {
                myCallback(it)
            })
        }
    }

    private fun request(request: Request, result: (result: String) -> Unit) {

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("RequestFail", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                result(response.body!!.string())
            }
        })
    }
}