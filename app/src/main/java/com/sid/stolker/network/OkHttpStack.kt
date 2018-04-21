package com.sid.stolker.network

import com.android.volley.AuthFailureError
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.toolbox.BaseHttpStack
import com.android.volley.toolbox.HttpResponse
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class OkHttpStack(private val mClient: OkHttpClient) : BaseHttpStack() {

    @Throws(AuthFailureError::class)
    private fun setConnectionParametersForRequest(builder: okhttp3.Request.Builder, request: Request<*>) {
        when (request.method) {
            Request.Method.DEPRECATED_GET_OR_POST -> {
                // Ensure backwards compatibility.  Volley assumes a request with a null body is a GET.
                val postBody = request.body
                if (postBody != null) {
                    builder.post(RequestBody.create(MediaType.parse(request.bodyContentType), postBody))
                }
            }
            Request.Method.GET -> builder.get()
            Request.Method.DELETE -> builder.delete()
            Request.Method.POST -> builder.post(createRequestBody(request)!!)
            Request.Method.PUT -> builder.put(createRequestBody(request)!!)
            Request.Method.HEAD -> builder.head()
            Request.Method.OPTIONS -> builder.method("OPTIONS", null)
            Request.Method.TRACE -> builder.method("TRACE", null)
            Request.Method.PATCH -> builder.patch(createRequestBody(request)!!)
            else -> throw IllegalStateException("Unknown method type.")
        }
    }

    @Throws(AuthFailureError::class)
    private fun createRequestBody(r: Request<*>): RequestBody? {
        val body = r.body ?: return null
        return RequestBody.create(MediaType.parse(r.bodyContentType), body)
    }

    @Throws(IOException::class, AuthFailureError::class)
    override fun executeRequest(request: Request<*>, additionalHeaders: Map<String, String>): com.android.volley.toolbox.HttpResponse? {
        val timeoutMs = request.timeoutMs
        val client = mClient.newBuilder()
                .connectTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
                .build()
        val okHttpRequestBuilder = okhttp3.Request.Builder()
        okHttpRequestBuilder.url(request.url)
        val headers = request.headers
        headers.forEach {
            okHttpRequestBuilder.addHeader(it.key, it.value)
        }
        additionalHeaders.forEach {
            okHttpRequestBuilder.addHeader(it.key, it.value)
        }
        setConnectionParametersForRequest(okHttpRequestBuilder, request)
        val okHttpRequest = okHttpRequestBuilder.build()
        val okHttpCall = client.newCall(okHttpRequest)
        val okHttpResponse = okHttpCall.execute()
        return HttpResponse(okHttpResponse.code(), okHttpResponse.headers().toMultimap().map {
            Header(it.key, it.value[0])
        }, okHttpResponse.body()?.contentLength()?.toInt()!!, okHttpResponse.body()?.byteStream())
    }
}