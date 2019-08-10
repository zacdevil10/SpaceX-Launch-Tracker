package uk.co.zac_h.spacex.statistics.graphs

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_total_launches_chart.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.statistics.adapters.StatisticsFilterListAdapter
import uk.co.zac_h.spacex.utils.data.LaunchesModel


class TotalLaunchesChartFragment : Fragment(), GraphsView {

    private lateinit var presenter: GraphsPresenter

    private var launches = ArrayList<LaunchesModel>()

    private var filterSuccessful = false
    private var filterFailed = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_total_launches_chart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = GraphsPresenterImpl(this, GraphsInteractorImpl())

        total_launches_filter_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = StatisticsFilterListAdapter(this@TotalLaunchesChartFragment, resources.getStringArray(R.array.filter_total_launches))
        }

        total_launches_pie_chart.apply {
            animateY(1400, Easing.EaseInOutCubic)
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

        presenter.getLaunchList("past")
    }

    private fun setData() {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        var falconOne = 0f
        var falconNine = 0f
        var falconHeavy = 0f

        launches.forEach {
            if (filterSuccessful && it.success != null && it.success!!) return@forEach
            if (filterFailed && !it.success!!) return@forEach

            when (it.rocket.id) {
                "falcon1" -> falconOne++
                "falcon9" -> falconNine++
                "falconheavy" -> falconHeavy++
            }
        }

        val range = "${launches[0].launchYear} - ${launches[launches.size - 1].launchYear}"

        total_launches_pie_chart.centerText = generateCenterSpannableText(range)

        entries.add(PieEntry(falconOne, "Falcon 1"))
        entries.add(PieEntry(falconNine, "Falcon 9"))
        entries.add(PieEntry(falconHeavy, "Falcon Heavy"))

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

        total_launches_pie_chart.data = data

        total_launches_pie_chart.invalidate()
    }

    private fun generateCenterSpannableText(range: String): SpannableString {
        val s = SpannableString("Falcon Launches\n$range")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 15, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 15, s.length - 11, 0)
        s.setSpan(RelativeSizeSpan(.8f), 15, s.length - 11, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.rgb("29b6f6")), s.length - 11, s.length, 0)
        return s
    }

    override fun setLaunchesList(launches: List<LaunchesModel>?) {
        if (launches != null) {
            this.launches.addAll(launches)
            setData()
        }
    }

    override fun updateLaunchesList(filter: String, isFiltered: Boolean) {
        when (filter) {
            "success" -> filterSuccessful = isFiltered
            "failed" -> filterFailed = isFiltered
        }

        setData()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
