package com.sid.stolker.network

import android.arch.lifecycle.MutableLiveData
import com.sid.stolker.models.StockPriceDataModel

class AlphaVantageWebService(private val requestQueue: RequestQueue) {

    val pricesData = MutableLiveData<StockPriceDataModel>()
    fun loadPrice() {
        /*requestQueue.addToRequestQueue()*/
    }
}