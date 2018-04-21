package com.sid.stolker.stockscreen

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.sid.stolker.models.StockPriceDataModel
import com.sid.stolker.network.AlphaVantageWebService

class StockPriceViewModel : ViewModel() {
    lateinit var alphaVantageWebService: AlphaVantageWebService

    fun initialize(alphaVantageWebService: AlphaVantageWebService): LiveData<StockPriceViewData> {
        this.alphaVantageWebService = alphaVantageWebService
        return Transformations.switchMap(alphaVantageWebService.pricesData,
                {
                    val viewData = MutableLiveData<StockPriceViewData>()
                    viewData.value = transformToViewData(it)
                    viewData
                }
        )
    }

    fun loadPrices(stockName: String) {
        alphaVantageWebService.loadPrice()
    }

    private fun transformToViewData(dataModel: StockPriceDataModel): StockPriceViewData {
        return StockPriceViewData()
    }
}