package com.sid.stolker.stockscreen

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sid.stolker.alphavantage.AlphaVantageWebService
import com.sid.stolker.persistence.AppDatabase

@Suppress("UNCHECKED_CAST")
class StockViewModelFactory(
        private val alphaVantageWebService: AlphaVantageWebService,
        private val appDatabaseInstance: AppDatabase) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm = StockPriceViewModel(alphaVantageWebService, appDatabaseInstance)
        return vm as T
    }
}