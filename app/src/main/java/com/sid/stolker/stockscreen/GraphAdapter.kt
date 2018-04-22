package com.sid.stolker.stockscreen

import com.robinhood.spark.SparkAdapter

class GraphAdapter : SparkAdapter() {

    private var dataPoints: List<Float> = emptyList()
    private var averageBaseline = 0F

    fun populate(graphPoints: List<Float>) {
        dataPoints = graphPoints
        averageBaseline = graphPoints.average().toFloat()
        notifyDataSetChanged()
    }

    override fun getBaseLine(): Float = averageBaseline

    override fun hasBaseLine(): Boolean = averageBaseline != 0F

    override fun getY(index: Int): Float = dataPoints[index]

    override fun getItem(index: Int): Any = dataPoints[index]

    override fun getCount(): Int = dataPoints.size
}