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
import java.util.*

class StockPriceViewModel : ViewModel() {
    private lateinit var alphaVantageWebService: AlphaVantageWebService
    private lateinit var closingTime: Date

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
        val openingPrice = timeSeriesToday.last().open
        val currentPrice = timeSeriesToday.first().close
        val closingPrice = if (hasMarketClosed())
            timeSeriesToday.first().close
        else
            null

        return StockPriceViewData(
                openingPrice,
                currentPrice,
                dayHigh, dayLow,
                closingPrice)
    }

    private fun hasMarketClosed(): Boolean = !Date().before(closingTime)

    @SuppressLint("SimpleDateFormat")
    private fun stripOtherDays(timeSeries: Map<String, TimeSeriesData>): ArrayList<TimeSeriesData> {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val todayMatcher = /*todo dateFormatter.format(Date())*/"2018-04-20"
        val strippedList = ArrayList<TimeSeriesData>()
        for (time in timeSeries) {
            if (time.key.startsWith(todayMatcher))
                strippedList.add(time.value)
            else {
                val timeFormat = SimpleDateFormat("hh:mm:ss")
                closingTime = timeFormat.parse(time.key.split(" ")[1])
                break
            }
        }
        return strippedList
    }

    private fun findDayHighAndLow(timeSeries: ArrayList<TimeSeriesData>): Pair<String, String> {
        var min = Float.MAX_VALUE
        var max = Float.MIN_VALUE
        timeSeries.forEach {
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