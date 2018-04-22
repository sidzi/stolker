package com.sid.stolker.stockscreen

import com.sid.stolker.alphavantage.AlphaVantageWebService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StockPriceViewModelTest {

    private lateinit var alphaVantageWebService: AlphaVantageWebService

    @Before
    fun setUp() {
        alphaVantageWebService = Mockito.mock(AlphaVantageWebService::class.java)
    }

    @Test
    fun loadIntraDayPrices() {
        val viewModel = StockPriceViewModel(alphaVantageWebService)
        viewModel.getData()
        viewModel.startIntradayPriceLoading("USA")
        Mockito.verify(alphaVantageWebService, Mockito.atLeastOnce())
                .loadPrice("?function=TIME_SERIES_INTRADAY&symbol=USA&interval=5min")
    }
}