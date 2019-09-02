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

class LaunchRateFragment : Fragment(), LaunchRateView {

    private lateinit var presenter: LaunchRatePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_launch_rate, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::presenter.isInitialized) presenter =
            LaunchRatePresenterImpl(this, LaunchRateInteractorImpl())

        launch_rate_falcon_one_toggle.setOnCheckedChangeListener { _, isChecked ->
            presenter.updateFilter("falcon1", isChecked)
        }
        launch_rate_falcon_nine_toggle.setOnCheckedChangeListener { _, isChecked ->
            presenter.updateFilter("falcon9", isChecked)
        }

        launch_rate_falcon_heavy_toggle.setOnCheckedChangeListener { _, isChecked ->
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
                textColor = Color.WHITE

            }
        }

        presenter.getLaunchList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
    }

    override fun updateBarChart(entries: ArrayList<BarEntry>, dataSize: Int) {
        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("FFFFFF"))

        val set = BarDataSet(entries, "Launches").apply {
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

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set)

        launch_rate_bar_chart.apply {
            xAxis.apply {
                setLabelCount(dataSize, true)
            }
            data = BarData(dataSets)
            invalidate()
        }
    }

    override fun showProgress() {
        launch_rate_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        launch_rate_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
