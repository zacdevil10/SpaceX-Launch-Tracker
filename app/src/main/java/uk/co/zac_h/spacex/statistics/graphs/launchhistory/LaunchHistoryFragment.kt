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
import uk.co.zac_h.spacex.utils.generateCenterSpannableText

class LaunchHistoryFragment : Fragment(), LaunchHistoryView {

    private lateinit var presenter: LaunchHistoryPresenter

    private var filterVisible: Boolean = false

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

        if (!::presenter.isInitialized) presenter =
            LaunchHistoryPresenterImpl(this, LaunchHistoryInteractorImpl())

        presenter.apply {
            getLaunchList("past")
            getRocketsList()
        }

        launch_history_success_toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) launch_history_failure_toggle.isChecked = false
            presenter.updateFilter("success", isChecked)
        }

        launch_history_failure_toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) launch_history_success_toggle.isChecked = false
            presenter.updateFilter("failed", isChecked)
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
        else -> super.onOptionsItemSelected(item)
    }

    override fun updatePieChart(
        entries: ArrayList<PieEntry>,
        centerText: String,
        animate: Boolean
    ) {
        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("9ccc65"))
        colors.add(ColorTemplate.rgb("ff7043"))

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

    override fun setSuccessRate(id: Int, percent: Int) {
        when (id) {
            1 -> {
                launch_history_falcon_one_rate_progress.progress = percent
                launch_history_falcon_one_percent_text.text =
                    context?.getString(R.string.percentage, percent)
            }
            2 -> {
                launch_history_falcon_nine_rate_progress.progress = percent
                launch_history_falcon_nine_percent_text.text =
                    context?.getString(R.string.percentage, percent)
            }
            3 -> {
                launch_history_falcon_heavy_rate_progress.progress = percent
                launch_history_falcon_heavy_percent_text.text =
                    context?.getString(R.string.percentage, percent)
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

    override fun showProgress() {
        launch_history_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        launch_history_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
