package com.sid.stolker.stockscreen

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.content_ticker.*

class StockPriceView(override val containerView: View) : Observer<StockPriceViewData>,
        LayoutContainer {

    @SuppressLint("SetTextI18n")
    override fun onChanged(viewData: StockPriceViewData?) {
        if (viewData == null) return
        tv_stock_name.text = viewData.stockName
        tv_closing_price.text = """Closing Price : ${viewData.closingPrice}"""
        tv_current_price.text = """Current Price : ${viewData.currentPrice}"""
        tv_highest_price.text = """Highest Price : ${viewData.highestPrice}"""
        tv_lowest_price.text = """Lowest Price : ${viewData.lowestPrice}"""
        tv_open_price.text = """Opening Price : ${viewData.openPrice}"""
    }
}