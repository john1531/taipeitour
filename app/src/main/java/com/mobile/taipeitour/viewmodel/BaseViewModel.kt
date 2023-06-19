package com.mobile.taipeitour.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    public abstract fun fetchData(params: Map<String, Any>)
}