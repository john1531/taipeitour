package com.mobile.taipeitour.viewmodel.attraction

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mobile.taipeitour.api.ApiService
import com.mobile.taipeitour.model.attraction.AttractionData
import com.mobile.taipeitour.model.attraction.AttractionsModel
import com.mobile.taipeitour.viewmodel.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttractionViewModel(application: Application) : BaseViewModel(application) {
    private val taipeitourApi: ApiService by lazy { ApiService(application) }
    val attractionsData = MutableLiveData<List<AttractionData>>()

    override fun fetchData(params: Map<String, Any>) {

        taipeitourApi.attractionsAPI(params["lang"] as String, params["page"] as Int).enqueue(object : Callback<AttractionsModel> {
            override fun onResponse( call: Call<AttractionsModel>, response: Response<AttractionsModel>)
            {
                response.body()?.let {
//                    if (it.code != "200") {
//                        Log.e(TAG, it.reason)
//                        return
//                    }
                    attractionsData.value = it.data
                }
            }
            override fun onFailure(call: Call<AttractionsModel>, t: Throwable) {

            }
        })
    }
}