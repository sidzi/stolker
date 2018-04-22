package com.sid.stolker.stockscreen

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.sid.stolker.IntentConstants
import com.sid.stolker.R
import com.sid.stolker.alphavantage.AlphaVantageWebService
import com.sid.stolker.alphavantage.Constants.ALPHA_VANTAGE_API_KEY
import com.sid.stolker.network.RequestPlacer
import com.sid.stolker.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_ticker.*
import kotlinx.android.synthetic.main.content_ticker.*

class StockPriceActivity : AppCompatActivity() {

    private var currentStock = "GOOG"
    private lateinit var stockPriceViewModel: StockPriceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticker)
        setSupportActionBar(toolbar)
        stockPriceViewModel =
                ViewModelProviders.of(this)
                        .get(StockPriceViewModel::class.java)
        val alphaVantageWebService =
                AlphaVantageWebService(
                        RequestPlacer::placeRequest,
                        ALPHA_VANTAGE_API_KEY,
                        RequestPlacer::cancelRequest)
        val stockPriceView = StockPriceView(cl_ticker_content)
        stockPriceViewModel.initialize(alphaVantageWebService)
                .observe(this, stockPriceView)
        stockPriceViewModel.startIntradayPriceLoading(currentStock)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_ticker, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.putExtra(IntentConstants.STOCK_SYMBOL, currentStock)
                startActivityForResult(intent, REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            data?.getStringExtra(IntentConstants.STOCK_SYMBOL)?.let {
                currentStock = it
                stockPriceViewModel.startIntradayPriceLoading(currentStock)
            }
    }

    companion object {
        private const val REQUEST_CODE = 10001
    }
}
