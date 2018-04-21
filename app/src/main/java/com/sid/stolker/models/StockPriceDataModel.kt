package com.sid.stolker.models

import com.squareup.moshi.Json

data class StockPriceDataModel(
        @Json(name = "Meta Data")
        val metadata: MetaData,
        @Json(name = "Time Series (5min)")
        val timeSeries: Map<String, TimeSeriesData>
)

data class MetaData(
        @Json(name = "2. Symbol")
        val symbol: String,
        @Json(name = "3. Last Refreshed")
        val lastRefresh: String,
        @Json(name = "4. Interval")
        val interval: String)

data class TimeSeriesData(
        @Json(name = "1. open")
        val open: String,
        @Json(name = "2. high")
        val high: String
)