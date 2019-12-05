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
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.model.spacex.RocketsModel
import uk.co.zac_h.spacex.utils.generateCenterSpannableText
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchHistoryFragment : Fragment(), LaunchHistoryView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private lateinit var presenter: LaunchHistoryPresenter

    private lateinit var networkStateChangeListener: OnNetworkStateChangeListener

    private var filterVisible = false
    private var filterSuccessful = false
    private var filterFailed = false

    private var launchesList = ArrayList<LaunchesModel>()
    private var rocketsList = ArrayList<RocketsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_launch_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchHistoryPresenterImpl(this, LaunchHistoryInteractorImpl())

        networkStateChangeListener = OnNetworkStateChangeListener(
            context
        ).apply {
            addListener(this@LaunchHistoryFragment)
            registerReceiver()
        }

        presenter.apply {
            if (launchesList.isEmpty()) getLaunchList() else addLaunchList(launchesList)
            if (rocketsList.isEmpty()) getRocketsList() else addRocketsList(rocketsList)
        }

        launch_history_success_toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) launch_history_failure_toggle.isChecked = false
            presenter.updateFilter(launchesList, "success", isChecked)
        }

        launch_history_failure_toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) launch_history_success_toggle.isChecked = false
            presenter.updateFilter(launchesList, "failed", isChecked)
        }

        //Pie chart appearance
        launch_history_pie_chart.apply {
            isDrawHoleEnabled = true
            setHoleColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setDrawEntryLabels(false)
            description.isEnabled = false
            setCenterTextColor(Color.WHITE)
            isRotationEnabled = false

            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                textColor = Color.WHITE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.showFilter(filterVisible)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
        networkStateChangeListener.removeListener(this)
        networkStateChangeListener.unregisterReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filter -> {
            presenter.showFilter(!filterVisible)
            true
        }
        R.id.reload -> {
            presenter.getLaunchList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updatePieChart(launches: ArrayList<LaunchesModel>, animate: Boolean) {
        launchesList.clear()
        launchesList.addAll(launches)
        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("9ccc65"))
        colors.add(ColorTemplate.rgb("ff7043"))

        val entries = ArrayList<PieEntry>()

        var falconOne = 0f
        var falconNine = 0f
        var falconHeavy = 0f

        launchesList.forEach {
            it.success?.let { success ->
                if (filterSuccessful && !success) return@forEach
                if (filterFailed && success) return@forEach
            }

            when (it.rocket.id) {
                "falcon1" -> falconOne++
                "falcon9" -> falconNine++
                "falconheavy" -> falconHeavy++
            }
        }

        entries.add(PieEntry(falconOne, "Falcon 1"))
        entries.add(PieEntry(falconNine, "Falcon 9"))
        entries.add(PieEntry(falconHeavy, "Falcon Heavy"))

        val centerText =
            "${launchesList[0].launchYear} - ${launchesList[launchesList.size - 1].launchYear}"

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
            this.centerText = context?.getString(R.string.pie_chart_title, centerText)
                ?.generateCenterSpannableText()
            this.data = data
            invalidate()
        }
    }

    override fun setSuccessRate(rockets: ArrayList<RocketsModel>) {
        rocketsList.clear()
        rockets.forEach {
            rocketsList.add(it)
            when (it.id) {
                1 -> {
                    launch_history_falcon_one_rate_progress.progress = it.successRate
                    launch_history_falcon_one_percent_text.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                2 -> {
                    launch_history_falcon_nine_rate_progress.progress = it.successRate
                    launch_history_falcon_nine_percent_text.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                3 -> {
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
            presenter.apply {
                if (launchesList.isEmpty()) getLaunchList()
                if (rocketsList.isEmpty()) getRocketsList()
            }
        }
    }
}
