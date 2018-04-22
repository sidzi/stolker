package com.sid.stolker.persistence

import android.content.Context

object SharedPreferencesLoader {

    fun getSavedStockSymbol(context: Context): String? {
        return context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
                .getString(SAVED_STOCK_SYMBOL, null)
    }

    fun saveStockSymbol(context: Context, symbol: String) {
        context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(SAVED_STOCK_SYMBOL, symbol)
                .apply()
    }

    private const val PREF_FILE = "stolker"

    private const val SAVED_STOCK_SYMBOL = "svd_stk_symbol"
}