package com.sid.stolker.alphavantage

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.sid.stolker.network.RequestPlacer
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class AlphaVantageWebServiceTest {

    @Suppress("unused")
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun loadPrice() {
        val webService = AlphaVantageWebService(PlaceRequestTestDouble::placeRequest, cancelRequest = RequestPlacer::cancelRequest)
        webService.loadPrice("?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=1min&apikey=demo")
        assert(webService.pricesData.value?.metadata?.interval == "1min",
                { webService.pricesData.value?.metadata?.interval ?: "null" })
        assert(webService.pricesData.value?.metadata?.interval != "5min",
                { webService.pricesData.value?.metadata?.interval ?: "null" })
        assert(webService.pricesData.value?.timeSeries?.contains("2018-04-20 16:00:00") ?: false,
                { webService.pricesData.value?.timeSeries?.keys?.toString() ?: "null" })
        assert(webService.pricesData.value?.timeSeries?.get("2018-04-20 16:00:00")?.open == "94.8900",
                { webService.pricesData.value?.timeSeries?.keys?.toString() ?: "null" })
    }
}