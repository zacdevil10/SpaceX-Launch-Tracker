package uk.co.zac_h.spacex.statistics.graphs.launchrate

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_launch_rate.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchRateFragment : Fragment(), LaunchRateView {

    private lateinit var presenter: LaunchRatePresenter

    private var launches = ArrayList<LaunchesModel>()

    private var filterFalconOne = true
    private var filterFalconNine = true
    private var filterFalconHeavy = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_launch_rate, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchRatePresenterImpl(this, LaunchRateInteractorImpl())

        launch_rate_falcon_one_toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.updateFilter("falcon1", isChecked)
        }
        launch_rate_falcon_nine_toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.updateFilter("falcon9", isChecked)
        }

        launch_rate_falcon_heavy_toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter.updateFilter("falconheavy", isChecked)
        }

        launch_rate_bar_chart.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "'" + value.toInt().toString().takeLast(2)
                    }
                }
                granularity = 1f
                setDrawGridLines(false)
            }
            axisLeft.apply {
                textColor = Color.WHITE
                granularity = 1f
                axisMinimum = 0f
            }
            axisRight.isEnabled = false
            setScaleEnabled(false)
            description.isEnabled = false
            setDrawBorders(false)

            legend.apply {
                textColor= Color.WHITE

            }
        }

        if (launches.isEmpty()) {
            presenter.getLaunchList()
        } else {
            setData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
    }

    private fun setData() {
        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("FFFFFF"))

        val dataSets = ArrayList<IBarDataSet>()

        val entries = ArrayList<BarEntry>()

        val dataMap = LinkedHashMap<Int, Int>()
        val dataMapFuture = LinkedHashMap<Int, Int>()

        for (i in 2006..launches[launches.size - 1].launchYear) {
            dataMap[i + 1] = 0
        }

        launches.forEach {
            if (!filterFalconOne && it.rocket.id == "falcon1") return@forEach
            if (!filterFalconNine && it.rocket.id == "falcon9") return@forEach
            if (!filterFalconHeavy && it.rocket.id == "falconheavy") return@forEach

            if (it.launchDateUnix.times(1000) <= System.currentTimeMillis()) {
                dataMap[it.launchYear + 1] = dataMap[it.launchYear + 1]?.plus(1) ?: 1
            } else {
                dataMapFuture[it.launchYear + 1] = dataMapFuture[it.launchYear + 1]?.plus(1) ?: 1
            }
        }

        dataMap.forEach {
            entries.add(BarEntry(it.key.toFloat(), floatArrayOf(it.value.toFloat(), dataMapFuture[it.key]?.toFloat() ?: 0f)))
        }

        val set = BarDataSet(entries, "Launches")
        set.apply {
            setColors(colors)
            valueTextColor = Color.WHITE
            valueTextSize = 9f
            valueFormatter = object : ValueFormatter() {
                override fun getBarStackedLabel(value: Float, stackedEntry: BarEntry): String {
                    val vals = stackedEntry.yVals
                    // find out if we are on top of the stack
                    return if (vals[vals.size - 1] == value) {
                        // return the "sum" across all stack values
                        "" + stackedEntry.y.toInt()
                    } else {
                        "" + value.toInt()
                    }
                }
            }
            stackLabels = arrayOf("Past", "Future")
        }

        dataSets.add(set)

        launch_rate_bar_chart.apply {
            xAxis.apply {
                setLabelCount(dataMap.size + 1, true)
            }
            data = BarData(dataSets)
            invalidate()
        }
    }

    override fun setLaunchesList(launches: List<LaunchesModel>?) {
        if (launches != null) {
            this.launches.addAll(launches)
            setData()
        }
    }

    override fun updateLaunchesList(filterId: String, isFiltered: Boolean) {
        when (filterId) {
            "falcon1" -> filterFalconOne = isFiltered
            "falcon9" -> filterFalconNine = isFiltered
            "falconheavy" -> filterFalconHeavy = isFiltered
        }

        if (launches.size > 0) setData()
    }

    override fun toggleProgress(visibility: Int) {
        launch_rate_progress_bar.visibility = visibility
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
