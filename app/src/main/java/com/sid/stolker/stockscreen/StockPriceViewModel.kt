package com.sid.stolker.stockscreen

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.sid.stolker.alphavantage.AVFunctions
import com.sid.stolker.alphavantage.AVQueryBuilder
import com.sid.stolker.alphavantage.AlphaVantageWebService
import com.sid.stolker.models.StockPriceDataModel
import com.sid.stolker.models.TimeSeriesData

class StockPriceViewModel : ViewModel() {
    private lateinit var alphaVantageWebService: AlphaVantageWebService

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

    fun loadIntraDayPrices(stockName: String) {
        val query = AVQueryBuilder(AVFunctions.TIME_SERIES_INTRADAY, stockName).build()
        alphaVantageWebService.loadPrice(query)
    }

    private fun transformToViewData(data: StockPriceDataModel): StockPriceViewData {
        val dayHigh = findDayHigh(data.timeSeries)
        return StockPriceViewData(data.timeSeries.entries.first().value.open, "", "$dayHigh", "", "")
    }

    private fun findDayHigh(timeSeries: Map<String, TimeSeriesData>): Any {
        return timeSeries.values.reduce { acc: TimeSeriesData, timeSeriesData: TimeSeriesData ->
            if (acc.high.toFloat() > timeSeriesData.high.toFloat())
                acc
            else
                timeSeriesData
        }.high
    }
}