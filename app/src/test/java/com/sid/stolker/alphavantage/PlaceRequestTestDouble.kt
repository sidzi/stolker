@file:Suppress("UNUSED_PARAMETER")

package com.sid.stolker.alphavantage

import com.android.volley.Response
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object PlaceRequestTestDouble {
    fun placeRequest(requestTag: String?, requestUrl: String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener) {
        val mockedResponse = getMockedResponse(requestUrl)
        responseListener.onResponse(mockedResponse)
    }

    fun cancelRequest(tag: String) {
    }

    private fun getMockedResponse(requestUrl: String): String {
        val requestPath = longHash(requestUrl)
        val fileContent = Files.readAllLines(
                Paths.get(
                        ClassLoader.getSystemClassLoader().getResource(requestPath.toString()).path))
        val response: String
        val stringBuilder = StringBuilder(1024)
        response = try {
            for (line in fileContent) {
                stringBuilder.append(line)
            }
            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
        return response
    }

    private fun longHash(string: String): Long {
        var h = 98764321261L
        val l = string.length
        val chars = string.toCharArray()

        for (i in 0 until l) {
            h = 31 * h + chars[i].toLong()
        }
        return h
    }
}