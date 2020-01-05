package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_launch_history.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.utils.generateCenterSpannableText
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchHistoryFragment : Fragment(), LaunchHistoryView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: LaunchHistoryPresenter? = null

    private var filterVisible = false
    private var filterSuccessful = false
    private var filterFailed = false

    private lateinit var launchStats: ArrayList<HistoryStatsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        launchStats =
            savedInstanceState?.getParcelableArrayList<HistoryStatsModel>("launches") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_launch_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchHistoryPresenterImpl(this, LaunchHistoryInteractorImpl())

        launch_history_chip_group.setOnCheckedChangeListener { group, checkedId ->
            println(launchStats)
            presenter?.updateFilter(
                launchStats,
                "success",
                launch_history_success_toggle.id == group.checkedChipId
            )
            presenter?.updateFilter(
                launchStats,
                "failed",
                launch_history_failure_toggle.id == group.checkedChipId
            )
        }

        presenter?.apply {
            if (launchStats.isEmpty()) getLaunchList() else addLaunchList(launchStats)
        }

        //Pie chart appearance
        launch_history_pie_chart.apply {
            isDrawHoleEnabled = true
            setHoleColor(ContextCompat.getColor(context, R.color.color_background))
            setDrawEntryLabels(false)
            description.isEnabled = false
            setCenterTextColor(ContextCompat.getColor(context, R.color.color_on_background))
            isRotationEnabled = false

            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
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
        outState.putParcelableArrayList("launches", launchStats)
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
            launchStats.clear()
            presenter?.getLaunchList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updatePieChart(stats: ArrayList<HistoryStatsModel>, animate: Boolean) {
        if (launchStats.isEmpty()) launchStats.addAll(stats)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("9ccc65"))
        colors.add(ColorTemplate.rgb("ff7043"))

        val entries = ArrayList<PieEntry>()

        var falconOne = 0
        var falconNine = 0
        var falconHeavy = 0

        stats.forEach {
            when (it.name) {
                "falcon1" -> falconOne = when {
                    filterSuccessful -> it.successes
                    filterFailed -> it.failures
                    else -> it.successes + it.failures
                }
                "falcon9" -> falconNine = when {
                    filterSuccessful -> it.successes
                    filterFailed -> it.failures
                    else -> it.successes + it.failures
                }
                "falconheavy" -> falconHeavy = when {
                    filterSuccessful -> it.successes
                    filterFailed -> it.failures
                    else -> it.successes + it.failures
                }
            }
        }

        if (falconOne > 0) entries.add(PieEntry(falconOne.toFloat(), "Falcon 1"))
        if (falconNine > 0) entries.add(PieEntry(falconNine.toFloat(), "Falcon 9"))
        if (falconHeavy > 0) entries.add(PieEntry(falconHeavy.toFloat(), "Falcon Heavy"))

        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 3f
            setColors(colors)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "" + value.toInt()
                }
            }
        }

        val data = PieData(dataSet).apply {
            setValueTextColor(Color.WHITE)
            setValueTextSize(11f)
        }

        launch_history_pie_chart.apply {
            if (animate) animateY(1400, Easing.EaseInOutCubic)
            this.centerText = context?.getString(R.string.pie_chart_title, "2006 - 2020")
                ?.generateCenterSpannableText()
            this.data = data
            invalidate()
        }
    }

    override fun setSuccessRate(stats: ArrayList<HistoryStatsModel>) {
        stats.forEach {
            when (it.name) {
                "falcon1" -> {
                    launch_history_falcon_one_rate_progress.progress = it.successRate
                    launch_history_falcon_one_percent_text.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                "falcon9" -> {
                    launch_history_falcon_nine_rate_progress.progress = it.successRate
                    launch_history_falcon_nine_percent_text.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                "falconheavy" -> {
                    launch_history_falcon_heavy_rate_progress.progress = it.successRate
                    launch_history_falcon_heavy_percent_text.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
            }
        }
    }

    override fun showFilter(filterVisible: Boolean) {
        launch_history_filter_constraint.visibility = when (filterVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        this.filterVisible = filterVisible
    }

    override fun setFilterSuccessful(isFiltered: Boolean) {
        filterSuccessful = isFiltered
    }

    override fun setFilterFailed(isFiltered: Boolean) {
        filterFailed = isFiltered
    }

    override fun showProgress() {
        launch_history_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        launch_history_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            presenter?.apply {
                if (launchStats.isEmpty() || launch_history_progress_bar.visibility == View.VISIBLE) getLaunchList()
            }
        }
    }
}
