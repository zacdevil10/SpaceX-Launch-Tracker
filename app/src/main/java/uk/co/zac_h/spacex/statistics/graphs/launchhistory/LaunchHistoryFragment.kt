package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchHistoryFragment : Fragment(), LaunchHistoryView {

    private lateinit var presenter: LaunchHistoryPresenter

    private var launches = ArrayList<LaunchesModel>()

    private var filterSuccessful = false
    private var filterFailed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_launch_history, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchHistoryPresenterImpl(
            this,
            LaunchHistoryInteractorImpl()
        )

        launch_history_success_toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) launch_history_failure_toggle.isChecked = false
            presenter.updateFilter("success", isChecked)
        }

        launch_history_failure_toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) launch_history_success_toggle.isChecked = false
            presenter.updateFilter("failed", isChecked)
        }

        launch_history_pie_chart.apply {
            isDrawHoleEnabled = true
            setHoleColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setDrawEntryLabels(false)
            description.isEnabled = false
            setCenterTextColor(Color.WHITE)

            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                textColor = Color.WHITE
            }
        }

        if (launches.isEmpty()) {
            presenter.getLaunchList("past")
        } else {
            setData(false)
        }

        presenter.getRocketsList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
    }

    private fun setData(animate: Boolean) {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        var falconOne = 0f
        var falconNine = 0f
        var falconHeavy = 0f

        launches.forEach {
            if (filterSuccessful && it.success != null && !it.success!!) return@forEach
            if (filterFailed && it.success!!) return@forEach

            when (it.rocket.id) {
                "falcon1" -> falconOne++
                "falcon9" -> falconNine++
                "falconheavy" -> falconHeavy++
            }
        }

        entries.add(PieEntry(falconOne, context?.getString(R.string.falcon_1)))
        entries.add(PieEntry(falconNine, context?.getString(R.string.falcon_9)))
        entries.add(PieEntry(falconHeavy, context?.getString(R.string.falcon_heavy)))

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("9ccc65"))
        colors.add(ColorTemplate.rgb("ff7043"))

        val dataSet = PieDataSet(entries, "")
        dataSet.apply {
            sliceSpace = 3f
            setColors(colors)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "" + value.toInt()
                }
            }
        }

        val data = PieData(dataSet)

        data.apply {
            setValueTextColor(Color.WHITE)
            setValueTextSize(11f)
        }

        launch_history_pie_chart.apply {
            if (animate) animateY(1400, Easing.EaseInOutCubic)
            centerText =
                generateCenterSpannableText("${launches[0].launchYear} - ${launches[launches.size - 1].launchYear}")
            this.data = data
            invalidate()
        }
    }

    private fun generateCenterSpannableText(range: String): SpannableString {
        val s = SpannableString(context?.getString(R.string.pie_chart_title, range))
        s.setSpan(RelativeSizeSpan(1.7f), 0, 8, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 8, s.length - 11, 0)
        s.setSpan(RelativeSizeSpan(.8f), 8, s.length - 11, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.rgb("29b6f6")), s.length - 11, s.length, 0)
        return s
    }

    override fun setLaunchesList(launches: List<LaunchesModel>?) {
        if (launches != null) {
            this.launches.addAll(launches)
            setData(animate = true)
        }
    }

    override fun updateLaunchesList(filter: String, isFiltered: Boolean) {
        when (filter) {
            "success" -> filterSuccessful = isFiltered
            "failed" -> filterFailed = isFiltered
        }

        setData(animate = false)
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun setSuccessRate(id: Int, percent: Int) {
        when (id) {
            1 -> {
                launch_history_falcon_one_rate_progress.progress = percent
                launch_history_falcon_one_percent_text.text = context?.getString(R.string.percentage, percent)
            }
            2 -> {
                launch_history_falcon_nine_rate_progress.progress = percent
                launch_history_falcon_nine_percent_text.text = context?.getString(R.string.percentage, percent)
            }
            3 -> {
                launch_history_falcon_heavy_rate_progress.progress = percent
                launch_history_falcon_heavy_percent_text.text = context?.getString(R.string.percentage, percent)
            }
        }
    }
}
