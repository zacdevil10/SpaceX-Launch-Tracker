package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import com.github.mikephil.charting.data.PieEntry

interface LaunchHistoryView {

    fun setSuccessRate(id: Int, percent: Int)

    fun updatePieChart(entries: ArrayList<PieEntry>, centerText: String, animate: Boolean)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)

}