package com.sid.stolker

import android.app.Application

import com.sid.stolker.network.RequestPlacer

class Stolker : Application() {

    override fun onCreate() {
        super.onCreate()
        RequestPlacer.init(this)
    }
}
