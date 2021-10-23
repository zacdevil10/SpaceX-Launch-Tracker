package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchHistoryBinding
import uk.co.zac_h.spacex.statistics.adapters.Statistics
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

class LaunchHistoryFragment : BaseFragment() {

    override val title by lazy { getString(Statistics.LAUNCH_HISTORY.title) }

    private lateinit var binding: FragmentLaunchHistoryBinding

    private val viewModel: LaunchHistoryViewModel by viewModels()

    private val navArgs: LaunchHistoryFragmentArgs by navArgs()

    private var launchStats: List<HistoryStatsModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host
        }

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchHistoryBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.get()

        binding.launchHistoryConstraint.transitionName = getString(navArgs.type.title)

        binding.tint.setOnClickListener {
            toggleFilterVisibility(false)
        }

        binding.launchHistoryChipGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setFilter(
                when (checkedId) {
                    binding.launchHistorySuccessToggle.id -> LaunchHistoryFilter.SUCCESSES
                    binding.launchHistoryFailureToggle.id -> LaunchHistoryFilter.FAILURES
                    else -> null
                }
            )
            viewModel.get()
        }

        //Pie chart appearance
        binding.launchHistoryPieChart.apply {
            isDrawHoleEnabled = true
            setHoleColor(ContextCompat.getColor(context, R.color.color_background))
            setDrawEntryLabels(false)
            description.isEnabled = false
            setCenterTextColor(ContextCompat.getColor(context, R.color.color_on_background))
            isRotationEnabled = false
            setTouchEnabled(false)

            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
            }
        }

        viewModel.launchHistory.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> response.data?.let {
                    val animate = viewModel.cacheLocation != Repository.RequestLocation.CACHE
                    update(animate, it)
                    setSuccessRate(it, animate)
                }
                ApiResult.Status.FAILURE -> showError(response.error?.message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        toggleFilterVisibility(viewModel.filterState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filter -> {
            toggleFilterVisibility(!viewModel.filterState)
            true
        }
        R.id.reload -> true.also { viewModel.get(CachePolicy.REFRESH) }
        else -> super.onOptionsItemSelected(item)
    }

    fun update(animate: Boolean, response: List<HistoryStatsModel>) {
        hideProgress()
        launchStats = response

        var falconOne = 0
        var falconNine = 0
        var falconHeavy = 0

        response.forEach {
            when (it.rocket) {
                RocketType.FALCON_ONE -> falconOne = when (viewModel.filterValue) {
                    LaunchHistoryFilter.SUCCESSES -> it.successes
                    LaunchHistoryFilter.FAILURES -> it.failures
                    else -> it.successes + it.failures
                }
                RocketType.FALCON_NINE -> falconNine = when (viewModel.filterValue) {
                    LaunchHistoryFilter.SUCCESSES -> it.successes
                    LaunchHistoryFilter.FAILURES -> it.failures
                    else -> it.successes + it.failures
                }
                RocketType.FALCON_HEAVY -> falconHeavy = when (viewModel.filterValue) {
                    LaunchHistoryFilter.SUCCESSES -> it.successes
                    LaunchHistoryFilter.FAILURES -> it.failures
                    else -> it.successes + it.failures
                }
            }
        }

        val entries = listOfNotNull(
            if (falconOne > 0) PieEntry(falconOne.toFloat(), "Falcon 1") else null,
            if (falconNine > 0) PieEntry(falconNine.toFloat(), "Falcon 9") else null,
            if (falconHeavy > 0) PieEntry(falconHeavy.toFloat(), "Falcon Heavy") else null
        )

        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 3f
            colors = listOf(
                ColorTemplate.rgb("29b6f6"),
                ColorTemplate.rgb("9ccc65"),
                ColorTemplate.rgb("ff7043"),
            )
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "" + value.toInt()
                }
            }
        }

        binding.launchHistoryPieChart.apply {
            centerText = getString(R.string.pie_chart_title, "2006 - 2020")
                .generateCenterSpannableText()
            data = PieData(dataSet).apply {
                setValueTextColor(Color.WHITE)
                setValueTextSize(11f)
            }
            if (animate) animateY(1400, Easing.EaseInOutCubic) else invalidate()
        }
    }

    private fun setSuccessRate(stats: List<HistoryStatsModel>, animate: Boolean) {
        stats.forEach {
            when (it.rocket) {
                RocketType.FALCON_ONE -> {
                    animateProgress(animate, it.successRate, binding.falconOneRateProgress)

                    binding.falconOnePercentText.text =
                        getString(R.string.percentage, it.successRate)
                }
                RocketType.FALCON_NINE -> {
                    animateProgress(animate, it.successRate, binding.falconNineRateProgress)

                    binding.falconNinePercentText.text =
                        getString(R.string.percentage, it.successRate)
                }
                RocketType.FALCON_HEAVY -> {
                    animateProgress(animate, it.successRate, binding.falconHeavyRateProgress)

                    binding.falconHeavyPercentText.text =
                        getString(R.string.percentage, it.successRate)
                }
            }
        }
    }

    private fun animateProgress(animate: Boolean, successRate: Int, progressBar: ProgressBar?) {
        if (animate) ValueAnimator.ofInt(0, successRate).apply {
            duration = 1000
            addUpdateListener { valueAnim ->
                progressBar?.progress = valueAnim.animatedValue as Int
            }
        }.start() else progressBar?.progress = successRate
    }

    private fun toggleFilterVisibility(filterVisible: Boolean) {
        binding.launchHistoryFilterConstraint.apply {
            when (filterVisible) {
                true -> {
                    visibility = View.VISIBLE
                    startAnimation(animateEnterFromTop(context))
                }
                false -> {
                    visibility = View.GONE
                    startAnimation(animateExitToTop(context))
                }
            }
        }

        binding.tint.apply {
            when (filterVisible) {
                true -> {
                    visibility = View.VISIBLE
                    startAnimation(animateFadeIn(context))
                }
                false -> {
                    visibility = View.GONE
                    startAnimation(animateFadeOut(context))
                }
            }
        }

        viewModel.showFilter(filterVisible)
    }

    fun showProgress() {

    }

    fun hideProgress() {

    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.get()
    }
}
