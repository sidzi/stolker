package com.sid.stolker.stockscreen

data class StockPriceViewData(
        var stockName:String,
        var openPrice: String,
        var currentPrice: String,
        var highestPrice: String,
        var lowestPrice: String,
        var closingPrice: String?,
        var marketStatus:Boolean
)