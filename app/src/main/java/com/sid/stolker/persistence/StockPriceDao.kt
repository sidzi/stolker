package com.sid.stolker.persistence

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sid.stolker.stockscreen.StockPriceViewData

@Dao
interface StockPriceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: StockPriceViewData)

    @Query("SELECT * FROM StockPriceViewData WHERE stockName = :stockName")
    fun query(stockName: String): StockPriceViewData

}