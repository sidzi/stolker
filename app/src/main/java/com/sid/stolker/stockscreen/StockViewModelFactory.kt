package com.sid.stolker.stockscreen

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sid.stolker.alphavantage.AlphaVantageWebService

@Suppress("UNCHECKED_CAST")
class StockViewModelFactory(private val alphaVantageWebService: AlphaVantageWebService) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm = StockPriceViewModel(alphaVantageWebService)
        return vm as T
    }
}