package com.sid.stolker.stockscreen

import com.robinhood.spark.SparkAdapter

class GraphAdapter : SparkAdapter() {

    private var dataPoints: List<Float> = emptyList()

    fun populate(graphPoints: List<Float>) {
        dataPoints = graphPoints
        notifyDataSetChanged()
    }

    override fun getBaseLine(): Float = 0F

    override fun hasBaseLine(): Boolean = true

    override fun getY(index: Int): Float = dataPoints[index]

    override fun getItem(index: Int): Any = dataPoints[index]

    override fun getCount(): Int = dataPoints.size
}