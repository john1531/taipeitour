package com.mobile.taipeitour.api

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.mobile.taipeitour.model.attraction.AttractionsModel
import com.mobile.taipeitour.utils.PrefUtil
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

class ApiService {
    companion object {
        public const val DOMAIN: String = "https://www.travel.taipei/open-api/"
    }

    constructor(context: Context?) {
        mContext = context
    }

    private var mContext: Context? = null

    val TaipeiTourApi: TaipeiTourApiService by lazy {
        val manager = RequestManager("${DOMAIN}")
        manager.getRetrofit(mContext).create(TaipeiTourApiService::class.java)
    }

    fun attractionsAPI(
        language: String,
        page: Int,
    ): Call<AttractionsModel> {
        return TaipeiTourApi.getAttractionsApi(language, page)
    }

    interface TaipeiTourApiService {
        @GET("{lang}/Attractions/All")
        fun getAttractionsApi(@Path("lang") language: String, @Query("page") page: Int): Call<AttractionsModel>
    }
}