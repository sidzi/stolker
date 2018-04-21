package com.sid.stolker.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

object RequestPlacer {
    lateinit var requestQueue: RequestQueue

    fun placeRequest(requestTag: String?, requestUrl: String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener) {
        val request = StringRequest(Request.Method.GET, requestUrl, responseListener, errorListener)
        requestQueue.addToRequestQueue(request, requestTag)
    }

    fun cancelRequest(tag:String){
        requestQueue.cancelRequestByTag(tag)
    }

    fun init(context: Context) {
        requestQueue = RequestQueue(context)
    }
}