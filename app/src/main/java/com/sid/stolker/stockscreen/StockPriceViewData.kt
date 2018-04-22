package com.sid.stolker.stockscreen

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class StockPriceViewData(
        @PrimaryKey
        var stockName: String,
        var openPrice: String,
        var currentPrice: String,
        var highestPrice: String,
        var lowestPrice: String,
        var closingPrice: String?,
        var marketStatus: Boolean,
        @Ignore
        var graphPoints: List<Float>
) {
    constructor() : this("", "", "", "", "", "", false, emptyList())
}