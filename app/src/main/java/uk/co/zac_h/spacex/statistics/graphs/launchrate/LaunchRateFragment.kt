package uk.co.zac_h.spacex.statistics.graphs.launchrate

import android.graphics.Color
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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_launch_rate.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchRateFragment : Fragment(), LaunchRateView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: LaunchRatePresenter? = null

    private var filterVisible: Boolean = false
    private var filterFalconOne = true
    private var filterFalconNine = true
    private var filterFalconHeavy = true

    private lateinit var launchesList: ArrayList<LaunchesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        launchesList =
            savedInstanceState?.getParcelableArrayList<LaunchesModel>("launches") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_launch_rate, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchRatePresenterImpl(this, LaunchRateInteractorImpl())

        launch_rate_falcon_one_toggle.setOnCheckedChangeListener { _, isChecked ->
            presenter?.updateFilter(launchesList, "falcon1", isChecked)
        }
        launch_rate_falcon_nine_toggle.setOnCheckedChangeListener { _, isChecked ->
            presenter?.updateFilter(launchesList, "falcon9", isChecked)
        }

        launch_rate_falcon_heavy_toggle.setOnCheckedChangeListener { _, isChecked ->
            presenter?.updateFilter(launchesList, "falconheavy", isChecked)
        }

        if (launchesList.isEmpty()) presenter?.getLaunchList() else presenter?.addLaunchList(
            launchesList
        )

        launch_rate_bar_chart.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "'" + value.toInt().toString().takeLast(2)
                    }
                }
                granularity = 1f
                setDrawGridLines(false)
            }
            axisLeft.apply {
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
                granularity = 1f
                axisMinimum = 0f
            }
            axisRight.isEnabled = false
            setScaleEnabled(false)
            description.isEnabled = false
            setDrawBorders(false)

            legend.apply {
                textColor = ContextCompat.getColor(context, R.color.color_on_background)

            }
        }
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onResume() {
        super.onResume()

        presenter?.showFilter(filterVisible)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("launches", launchesList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filter -> {
            presenter?.showFilter(!filterVisible)
            true
        }
        R.id.reload -> {
            presenter?.getLaunchList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateBarChart(launches: ArrayList<LaunchesModel>, animate: Boolean) {
        launchesList.clear()
        launchesList.addAll(launches)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("b00020"))

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
                dataMapFuture[it.launchYear + 1] =
                    dataMapFuture[it.launchYear + 1]?.plus(1) ?: 1
            }
        }

        dataMap.forEach {
            entries.add(
                BarEntry(
                    it.key.toFloat(),
                    floatArrayOf(it.value.toFloat(), dataMapFuture[it.key]?.toFloat() ?: 0f)
                )
            )
        }

        val set = BarDataSet(entries, "Launches").apply {
            setColors(colors)
            valueTextColor =
                context?.let { ContextCompat.getColor(it, R.color.color_on_background) }
                    ?: Color.BLUE
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
            if (animate) animateY(400, Easing.Linear)
            xAxis.apply {
                setLabelCount(dataMap.size + 1, true)
            }
            data = BarData(dataSets)
            invalidate()
        }
    }

    override fun showFilter(filterVisible: Boolean) {
        launch_rate_filter_scroll.visibility = when (filterVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        this.filterVisible = filterVisible
    }

    override fun setFalconOneFilter(isFiltered: Boolean) {
        filterFalconOne = isFiltered
    }

    override fun setFalconNineFilter(isFiltered: Boolean) {
        filterFalconNine = isFiltered
    }

    override fun setFalconHeavyFilter(isFiltered: Boolean) {
        filterFalconHeavy = isFiltered
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
            if (launchesList.isEmpty() || launch_rate_progress_bar.visibility == View.VISIBLE) presenter?.getLaunchList()
        }
    }
}
