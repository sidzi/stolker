package com.sid.stolker

import android.app.Application

import com.sid.stolker.network.RequestPlacer
import com.sid.stolker.persistence.DatabaseManager

class Stolker : Application() {

    override fun onCreate() {
        super.onCreate()
        RequestPlacer.init(this)
        DatabaseManager.appContext = this
    }
}
