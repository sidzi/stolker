package com.sid.stolker.stockscreen

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.os.Handler
import com.sid.stolker.alphavantage.AlphaVantageWebService
import com.sid.stolker.persistence.AppDatabase
import com.sid.stolker.persistence.StockPriceDao_Impl
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StockPriceViewModelTest {

    @Suppress("unused")
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var alphaVantageWebService: AlphaVantageWebService
    private lateinit var appDatabaseInstance: AppDatabase
    private lateinit var handler: Handler

    @Before
    fun setUp() {
        alphaVantageWebService = Mockito.mock(AlphaVantageWebService::class.java)
        appDatabaseInstance = Mockito.mock(AppDatabase::class.java)
        Mockito.`when`(appDatabaseInstance.stockPriceDao())
                .thenReturn(Mockito.mock(StockPriceDao_Impl::class.java))
        Mockito.`when`(appDatabaseInstance.stockPriceDao().query("USA"))
                .thenReturn(StockPriceViewData())
        handler = Mockito.mock(Handler::class.java)
    }

    @Test
    fun loadIntraDayPrices() {
        val viewModel = StockPriceViewModel(alphaVantageWebService, appDatabaseInstance, handler)
        viewModel.getData()
        viewModel.startIntradayPriceLoading("USA")
        Mockito.verify(alphaVantageWebService, Mockito.atLeastOnce())
                .loadPrice("?function=TIME_SERIES_INTRADAY&symbol=USA&interval=5min")
        Mockito.verify(handler, Mockito.atLeastOnce())
                .postDelayed(Mockito.any(), Mockito.eq(1000 * 30 * 1L))
    }
}