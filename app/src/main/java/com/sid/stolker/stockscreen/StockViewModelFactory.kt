package com.sid.stolker.stockscreen

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Handler
import com.sid.stolker.alphavantage.AlphaVantageWebService
import com.sid.stolker.persistence.AppDatabase

@Suppress("UNCHECKED_CAST")
class StockViewModelFactory(
        private val alphaVantageWebService: AlphaVantageWebService,
        private val appDatabaseInstance: AppDatabase,
        private val handler: Handler) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm = StockPriceViewModel(alphaVantageWebService, appDatabaseInstance, handler)
        return vm as T
    }
}