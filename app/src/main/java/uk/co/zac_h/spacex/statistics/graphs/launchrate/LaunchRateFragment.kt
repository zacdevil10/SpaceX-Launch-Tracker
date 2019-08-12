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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_launch_rate, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchRatePresenterImpl(this, LaunchRateInteractorImpl())

        launch_rate_line_chart.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = Color.WHITE
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "'" + value.toInt().toString().takeLast(2)
                    }
                }
                setDrawGridLines(false)
            }
            axisLeft.apply {
                textColor = Color.WHITE
            }
            axisRight.isEnabled = false
            description.isEnabled = false
            setDrawBorders(false)
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
        val dataSets = ArrayList<IBarDataSet>()
        val entries = ArrayList<BarEntry>()
        val dataMap = LinkedHashMap<Int, Int>()

        launches.forEach {
            dataMap[it.launchYear + 1] = dataMap[it.launchYear + 1]?.plus(1) ?: 1
        }

        println(dataMap)

        dataMap.forEach {
            entries.add(BarEntry(it.key.toFloat(), it.value.toFloat()))
        }

        val set = BarDataSet(entries, "")
        set.apply {
            color = ColorTemplate.rgb("29b6f6")
            valueTextColor = Color.WHITE
            valueTextSize = 9f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "" + value.toInt()
                }
            }
        }
        dataSets.add(set)

        launch_rate_line_chart.apply {
            xAxis.setLabelCount(dataMap.size + 2, true)
            xAxis.setCenterAxisLabels(true)
            data = BarData(dataSets)
        }
    }

    override fun setLaunchesList(launches: List<LaunchesModel>?) {
        if (launches != null) {
            this.launches.addAll(launches)
            setData()
        }
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
