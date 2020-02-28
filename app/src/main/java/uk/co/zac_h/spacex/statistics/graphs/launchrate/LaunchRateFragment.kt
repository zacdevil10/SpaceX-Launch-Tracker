package uk.co.zac_h.spacex.statistics.graphs.launchrate

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_launch_rate.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.utils.models.RateStatsModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchRateFragment : Fragment(), LaunchRateView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: LaunchRatePresenter? = null

    private lateinit var statsList: ArrayList<RateStatsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        statsList =
            savedInstanceState?.getParcelableArrayList<RateStatsModel>("launches") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_launch_rate, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchRatePresenterImpl(this, LaunchRateInteractorImpl())

        if (statsList.isEmpty()) presenter?.getLaunchList()
        else presenter?.addLaunchList(statsList)

        launch_rate_bar_chart.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "'" + value.toInt().toString().takeLast(2)
                    }
                }
                setDrawGridLines(false)
            }
            axisLeft.apply {
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
                isGranularityEnabled = true
                granularity = 1f
                axisMinimum = 0f
            }
            axisRight.isEnabled = false
            setScaleEnabled(false)
            description.isEnabled = false
            setDrawBorders(false)
            isHighlightFullBarEnabled = true
            legend.apply {
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
            }

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val stats = statsList[(e.x - 2006).toInt()]

                        launch_rate_key.visibility = View.VISIBLE

                        launch_rate_year.text = stats.year.toString()

                        launch_rate_falcon_one_label.visibility =
                            if (stats.falconOne == 0f) View.GONE else View.VISIBLE
                        launch_rate_falcon_one_value.visibility =
                            if (stats.falconOne == 0f) View.GONE else View.VISIBLE
                        launch_rate_falcon_one_value.text = stats.falconOne.toInt().toString()

                        launch_rate_falcon_nine_label.visibility =
                            if (stats.falconNine == 0f) View.GONE else View.VISIBLE
                        launch_rate_falcon_nine_value.visibility =
                            if (stats.falconNine == 0f) View.GONE else View.VISIBLE
                        launch_rate_falcon_nine_value.text = stats.falconNine.toInt().toString()

                        launch_rate_falcon_heavy_label.visibility =
                            if (stats.falconHeavy == 0f) View.GONE else View.VISIBLE
                        launch_rate_falcon_heavy_value.visibility =
                            if (stats.falconHeavy == 0f) View.GONE else View.VISIBLE
                        launch_rate_falcon_heavy_value.text = stats.falconHeavy.toInt().toString()

                        launch_rate_failures_label.visibility =
                            if (stats.failure == 0f) View.GONE else View.VISIBLE
                        launch_rate_failures_value.visibility =
                            if (stats.failure == 0f) View.GONE else View.VISIBLE
                        launch_rate_failures_value.text = stats.failure.toInt().toString()

                        launch_rate_future_label.visibility =
                            if (stats.planned == 0f) View.GONE else View.VISIBLE
                        launch_rate_future_value.visibility =
                            if (stats.planned == 0f) View.GONE else View.VISIBLE
                        launch_rate_future_value.text = stats.planned.toInt().toString()

                        launch_rate_total_value.text = e.y.toInt().toString()
                    }
                }

                override fun onNothingSelected() {
                    launch_rate_key.visibility = View.GONE
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("launches", statsList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_pads, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            statsList.clear()
            presenter?.getLaunchList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateBarChart(stats: List<RateStatsModel>, animate: Boolean) {
        if (statsList.isEmpty()) statsList.addAll(stats)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6")) //F1
        colors.add(ColorTemplate.rgb("9ccc65")) //F9
        colors.add(ColorTemplate.rgb("ff7043")) //FH
        colors.add(ColorTemplate.rgb("b00020")) //Failures
        colors.add(ColorTemplate.rgb("66bb6a")) //Future

        val entries = ArrayList<BarEntry>()

        var max = 0f

        stats.forEach {
            val newMax = it.falconOne + it.falconNine + it.falconHeavy + it.failure + it.planned
            if (newMax > max) max = newMax
            entries.add(
                BarEntry(
                    it.year.toFloat(),
                    floatArrayOf(
                        it.falconOne,
                        it.falconNine,
                        it.falconHeavy,
                        it.failure,
                        it.planned
                    )
                )
            )
        }

        val set = BarDataSet(entries, "").apply {
            setColors(colors)
            setDrawValues(false)

            stackLabels = arrayOf("Falcon 1", "Falcon 9", "Falcon Heavy", "Failures", "Future")
        }

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set)

        launch_rate_bar_chart.apply {
            if (animate) animateY(400, Easing.Linear)
            xAxis.labelCount = stats.size
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = if ((max.toInt() % 2) == 0) max else max.plus(1)
                labelCount =
                    if ((max.toInt() % 2) == 0) max.toInt() / 2 else max.toInt().plus(1) / 2
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

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (statsList.isEmpty() || launch_rate_progress_bar.visibility == View.VISIBLE) presenter?.getLaunchList()
        }
    }
}
