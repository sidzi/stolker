package com.sid.stolker.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sid.stolker.IntentConstants
import com.sid.stolker.R
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        et_stock_symbol_input.setText(intent.getStringExtra(IntentConstants.STOCK_SYMBOL))
        b_ok.setOnClickListener {
            val intent = Intent()
            intent.putExtra(IntentConstants.STOCK_SYMBOL,
                    et_stock_symbol_input.text.toString().trim())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

}
