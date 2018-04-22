package com.sid.stolker.stockscreen

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.view.View
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.content_ticker.*

class StockPriceView(override val containerView: View, private val graphAdapter: GraphAdapter) : Observer<StockPriceViewData>,
        LayoutContainer {

    init {
        sv_price_graph.adapter = graphAdapter
    }

    @SuppressLint("SetTextI18n")
    override fun onChanged(viewData: StockPriceViewData?) {
        if (viewData == null) {
            return /* Can display error state here */
        } else {
            tv_stock_name.text = viewData.stockName
            tv_opening_price.text = """Opening Price : ${viewData.openPrice}"""
            tv_current_price.text = """Current Price : ${viewData.currentPrice}"""
            tv_highest_price.text = """Highest Price : ${viewData.highestPrice}"""
            tv_lowest_price.text = """Lowest Price : ${viewData.lowestPrice}"""
            tv_closing_price.text = """Closing Price : ${viewData.closingPrice}"""
            tv_market_status.text = """Market ${if (viewData.marketStatus) "Open" else "Closed"}"""
            graphAdapter.populate(viewData.graphPoints)
        }
        loadComplete()
    }

    fun loadingStart() {
        pb_loading.visibility = View.VISIBLE
    }

    private fun loadComplete() {
        pb_loading.visibility = View.GONE
    }
}