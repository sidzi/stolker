package com.sid.stolker.stockscreen

import android.arch.lifecycle.Observer
import android.util.Log
import android.view.View

class StockPriceView(view: View) : Observer<StockPriceViewData> {

    override fun onChanged(viewData: StockPriceViewData?) {
        Log.d("viewData", viewData?.toString())
    }
}