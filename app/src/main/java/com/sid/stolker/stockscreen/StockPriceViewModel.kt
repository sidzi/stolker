package com.sid.stolker.stockscreen

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.sid.stolker.alphavantage.AVFunctions
import com.sid.stolker.alphavantage.AVQueryBuilder
import com.sid.stolker.alphavantage.AlphaVantageWebService
import com.sid.stolker.models.StockPriceDataModel
import com.sid.stolker.models.TimeSeriesData
import java.text.SimpleDateFormat

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

    private fun transformToViewData(data: StockPriceDataModel): StockPriceViewData? {
        val timeSeriesToday = stripOtherDays(data.timeSeries)
        if (timeSeriesToday.isEmpty()) return null

        val (dayHigh, dayLow) = findDayHighAndLow(timeSeriesToday)
        val openingPrice = timeSeriesToday.entries.last().value.open
        val currentPrice = timeSeriesToday.entries.first().value.close
        val closingPrice = if (hasMarketClosed())
            timeSeriesToday.entries.first().value.close
        else
            null

        return StockPriceViewData(
                openingPrice,
                currentPrice,
                dayHigh, dayLow,
                closingPrice)
    }

    private fun hasMarketClosed(): Boolean = true /*find open/closing times by tracking day change*/

    @SuppressLint("SimpleDateFormat")
    private fun stripOtherDays(timeSeries: Map<String, TimeSeriesData>): Map<String, TimeSeriesData> {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val todayMatcher = /*dateFormatter.format(Date())*/"2018-04-20"
        return timeSeries.filterKeys {
            it.startsWith(todayMatcher)
        }
    }

    private fun findDayHighAndLow(timeSeries: Map<String, TimeSeriesData>): Pair<String, String> {
        var min = Float.MAX_VALUE
        var max = Float.MIN_VALUE
        timeSeries.values.forEach {
            val low = it.low.toFloat()
            val high = it.high.toFloat()
            if (low < min)
                min = low
            if (high > max)
                max = high
        }
        return Pair("%.4f".format(max), "%.4f".format(min))
    }
}