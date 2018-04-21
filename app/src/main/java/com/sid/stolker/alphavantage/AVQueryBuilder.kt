package com.sid.stolker.alphavantage

class AVQueryBuilder(
        private val function: AVFunctions,
        private val symbol: String,
        private val interval: String = "5min") {

    fun build(): String {
        return "?function=$function&symbol=$symbol&interval=$interval"
    }
}