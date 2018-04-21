package com.sid.stolker.stockscreen

import android.arch.lifecycle.Observer
import android.util.Log
import android.view.View
import kotlinx.android.extensions.LayoutContainer

class StockPriceView(override val containerView: View?) : Observer<StockPriceViewData>,
        LayoutContainer {

    override fun onChanged(viewData: StockPriceViewData?) {
        Log.d("viewData", viewData?.toString())
    }
}