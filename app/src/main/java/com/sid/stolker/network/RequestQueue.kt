package com.sid.stolker.network

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import okhttp3.Cache
import okhttp3.OkHttpClient

class RequestQueue(applicationContext: Context) {
    private val mRequestQueue: RequestQueue

    init {
        val client = OkHttpClient.Builder()
                .cache(Cache(applicationContext.cacheDir, CACHE_SIZE_BYTES))
                .build()
        mRequestQueue = Volley.newRequestQueue(applicationContext,
                OkHttpStack(client))
    }

    fun addToRequestQueue(req: Request<*>, requestTag: String?) {
        req.retryPolicy = REQUEST_RETRY_POLICY
        req.tag = requestTag ?: "default"
        mRequestQueue.add(req)
    }

    fun cancelRequestByTag(tag: String) {
        mRequestQueue.cancelAll(tag)
    }

    companion object {
        private val REQUEST_RETRY_POLICY = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        private const val CACHE_SIZE_BYTES = 1024 * 1024 * 2L
    }

}