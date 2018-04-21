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
    fun loadPrices() {
        val viewModel = StockPriceViewModel()
        viewModel.initialize(alphaVantageWebService)
        viewModel.loadIntraDayPrices("USA")
        Mockito.verify(alphaVantageWebService, Mockito.atLeastOnce()).loadPrice("USA")
    }
}