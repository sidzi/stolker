package com.sid.stolker.stockscreen

data class StockPriceViewData(
        var openPrice: String,
        var currentPrice: String,
        var highestPrice: String,
        var lowestPrice: String,
        var closingPrice: String?
) {
    override fun toString(): String {
        return "StockPriceModel(openPrice='$openPrice', currentPrice='$currentPrice', highestPrice='$highestPrice', lowestPrice='$lowestPrice', closingPrice='$closingPrice')"
    }
}