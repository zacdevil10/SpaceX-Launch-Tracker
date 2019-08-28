package uk.co.zac_h.spacex.statistics.graphs.launchrate

import com.github.mikephil.charting.data.BarEntry

interface LaunchRateView {

    fun updateBarChart(entries: ArrayList<BarEntry>, dataSize: Int)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}