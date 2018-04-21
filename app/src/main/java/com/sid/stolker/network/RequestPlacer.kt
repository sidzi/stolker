package com.sid.stolker.network

import android.content.Context

object RequestPlacer {
    lateinit var requestQueue: RequestQueue
    fun init(context: Context) {
        requestQueue = RequestQueue(context)
    }
}