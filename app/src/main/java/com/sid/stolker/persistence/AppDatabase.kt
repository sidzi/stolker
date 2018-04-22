package com.sid.stolker.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.sid.stolker.stockscreen.StockPriceViewData

@Database(entities = [(StockPriceViewData::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    internal abstract fun stockPriceDao(): StockPriceDao
}