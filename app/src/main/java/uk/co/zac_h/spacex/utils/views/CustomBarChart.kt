package uk.co.zac_h.spacex.utils.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import uk.co.zac_h.spacex.R

class CustomBarChart : BarChart {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun setup() {
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
            setDrawGridLines(false)
        }
        axisRight.isEnabled = false
        setScaleEnabled(false)
        description.isEnabled = false
        setDrawBorders(false)
        isHighlightFullBarEnabled = true
        legend.apply {
            textColor = ContextCompat.getColor(context, R.color.color_on_background)
        }
    }

}