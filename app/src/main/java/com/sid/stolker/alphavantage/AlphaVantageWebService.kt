package com.sid.stolker.alphavantage

import MoshiSerializer
import android.arch.lifecycle.MutableLiveData
import com.android.volley.Response
import com.sid.stolker.models.StockPriceDataModel
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction4

class AlphaVantageWebService(
        private val placeRequest: KFunction4<@ParameterName(name = "requestTag") String?, @ParameterName(name = "requestUrl") String, @ParameterName(name = "responseListener") Response.Listener<String>, @ParameterName(name = "errorListener") Response.ErrorListener, Unit>,
        private val apiKey: String = "demo",
        private val cancelRequest: KFunction1<@ParameterName(name = "tag") String, Unit>?) {

    val pricesData = MutableLiveData<StockPriceDataModel>()
    fun loadPrice(getQuery: String) {
        val requestUrl = "$BASE_URL$getQuery&apikey=$apiKey"
        cancelRequest?.invoke(REQUEST_TAG_LP)
        placeRequest(REQUEST_TAG_LP, requestUrl, Response.Listener {
            val response = MoshiSerializer.serialize(StockPriceDataModel::class.java, it) as StockPriceDataModel?
            pricesData.value = response
        }, Response.ErrorListener {
            System.out.println(it.message)
        })
    }

    companion object {
        private const val REQUEST_TAG_LP = "loadPrice"
        private const val BASE_URL = "https://www.alphavantage.co/query"
    }
}