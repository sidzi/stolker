package com.sid.stolker.stockscreen

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.sid.stolker.alphavantage.AVFunctions
import com.sid.stolker.alphavantage.AVQueryBuilder
import com.sid.stolker.alphavantage.AlphaVantageWebService
import com.sid.stolker.models.StockPriceDataModel
import com.sid.stolker.models.TimeSeriesData
import com.sid.stolker.persistence.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class StockPriceViewModel(
        private val alphaVantageWebService: AlphaVantageWebService,
        private val appDatabaseInstance: AppDatabase,
        private val handler: Handler
) : ViewModel() {

    private var marketClosed = false
    private var cachedStockQuery: String? = null
    private val stockPriceViewData = Transformations.switchMap(alphaVantageWebService.pricesData, {
        val temp = MutableLiveData<StockPriceViewData>()
        val transformedData = transformToViewData(it)
        temp.value = transformedData
        transformedData?.let {
            Thread {
                appDatabaseInstance.stockPriceDao().insert(it)
            }.start()
        }
        temp
    }) as MutableLiveData

    fun getData(): LiveData<StockPriceViewData> {
        return stockPriceViewData
    }

    fun startIntradayPriceLoading(stockName: String) {
        val query = AVQueryBuilder(AVFunctions.TIME_SERIES_INTRADAY, stockName).build()
        if (query != cachedStockQuery) {
            Thread {
                this.stockPriceViewData.postValue(appDatabaseInstance.stockPriceDao().query(stockName))
            }.start()
            cachedStockQuery = query
            alphaVantageWebService.loadPrice(query)
            val runnable = object : Runnable {
                override fun run() {
                    if (!isMarketClosed()) {
                        alphaVantageWebService.loadPrice(query)
                        handler.postDelayed(this, POLLING_TIME)
                    } else
                        handler.removeCallbacksAndMessages(null)
                }
            }
            handler.postDelayed(runnable, POLLING_TIME)
        } else {
            if (alphaVantageWebService.pricesData.value != null)
                alphaVantageWebService.pricesData.value = alphaVantageWebService.pricesData.value
        }
    }

    private fun isMarketClosed(): Boolean = marketClosed

    private fun transformToViewData(data: StockPriceDataModel): StockPriceViewData? {
        val stockName = data.metadata.symbol
        marketClosed = checkIfMarketHasClosed(data.timeSeries.entries.first())
        val timeSeriesToday = stripOtherDays(data.timeSeries)
        if (timeSeriesToday.isEmpty()) return null
        val (dayHigh, dayLow) = findDayHighAndLow(timeSeriesToday)
        val openingPrice = timeSeriesToday.last().open
        val currentPrice = timeSeriesToday.first().close
        val closingPrice = if (isMarketClosed())
            timeSeriesToday.first().close
        else
            null

        val dataPoints = timeSeriesToday.map {
            it.open.toFloat()
        }.asReversed()

        return StockPriceViewData(
                stockName,
                openingPrice,
                currentPrice,
                dayHigh, dayLow,
                closingPrice,
                !isMarketClosed(),
                dataPoints)
    }

    @SuppressLint("SimpleDateFormat")
    private fun checkIfMarketHasClosed(firstEntry: Map.Entry<String, TimeSeriesData>): Boolean {
        val timeFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val recentMostTime = timeFormat.parse(firstEntry.key)
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, -6)
        val date = cal.time
        return recentMostTime.before(date)
    }

    @SuppressLint("SimpleDateFormat")
    private fun stripOtherDays(timeSeries: Map<String, TimeSeriesData>): ArrayList<TimeSeriesData> {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val todayMatcher = /* todo dateFormatter.format(Date())*/"2018-04-20"
        val strippedList = ArrayList<TimeSeriesData>()
        for (time in timeSeries) {
            if (time.key.startsWith(todayMatcher))
                strippedList.add(time.value)
            else
                break
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

    companion object {
        private const val POLLING_TIME = 1000 * 30 * 1L
    }
}