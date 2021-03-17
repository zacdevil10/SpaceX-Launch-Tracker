package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchHistoryBinding
import uk.co.zac_h.spacex.statistics.adapters.Statistics
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel

class LaunchHistoryFragment : BaseFragment(), LaunchHistoryContract.LaunchHistoryView {

    override val title: String by lazy { Statistics.LAUNCH_HISTORY.title }

    private var _binding: FragmentLaunchHistoryBinding? = null
    private val binding get() = _binding!!

    private var presenter: LaunchHistoryContract.LaunchHistoryPresenter? = null

    private var filter: LaunchHistoryFilter? = null
    private var filterVisible = false

    private lateinit var launchStats: ArrayList<HistoryStatsModel>

    private var heading: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()

        heading = arguments?.getString("heading")
        launchStats = savedInstanceState?.getParcelableArrayList("launches") ?: ArrayList()
        filterVisible = savedInstanceState?.getBoolean("filter") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchHistoryBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
        binding.toolbarLayout.toolbar.setup()

        binding.launchHistoryConstraint.transitionName = heading

        binding.tint.setOnClickListener {
            toggleFilterVisibility(false)
        }

        presenter = LaunchHistoryPresenterImpl(this, LaunchHistoryInteractorImpl())

        binding.launchHistoryChipGroup.setOnCheckedChangeListener { _, checkedId ->
            filter = when (checkedId) {
                binding.launchHistorySuccessToggle.id -> LaunchHistoryFilter.SUCCESSES
                binding.launchHistoryFailureToggle.id -> LaunchHistoryFilter.FAILURES
                else -> null
            }
            presenter?.updateFilter(launchStats)
        }

        presenter?.getOrUpdate(launchStats)

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
    }

    override fun onResume() {
        super.onResume()
        presenter?.showFilter(filterVisible)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putParcelableArrayList("launches", launchStats)
            putBoolean("filter", filterVisible)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filter -> {
            presenter?.showFilter(!filterVisible)
            true
        }
        R.id.reload -> {
            apiState = ApiState.PENDING
            launchStats.clear()
            presenter?.getOrUpdate(null)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(data: Any, response: List<HistoryStatsModel>) {
        apiState = ApiState.SUCCESS
        if (launchStats.isEmpty()) launchStats.addAll(response)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6"))
        colors.add(ColorTemplate.rgb("9ccc65"))
        colors.add(ColorTemplate.rgb("ff7043"))

        val entries = ArrayList<PieEntry>()

        var falconOne = 0
        var falconNine = 0
        var falconHeavy = 0

        response.forEach {
            when (it.rocket) {
                RocketType.FALCON_ONE -> falconOne = when (filter) {
                    LaunchHistoryFilter.SUCCESSES -> it.successes
                    LaunchHistoryFilter.FAILURES -> it.failures
                    else -> it.successes + it.failures
                }
                RocketType.FALCON_NINE -> falconNine = when (filter) {
                    LaunchHistoryFilter.SUCCESSES -> it.successes
                    LaunchHistoryFilter.FAILURES -> it.failures
                    else -> it.successes + it.failures
                }
                RocketType.FALCON_HEAVY -> falconHeavy = when (filter) {
                    LaunchHistoryFilter.SUCCESSES -> it.successes
                    LaunchHistoryFilter.FAILURES -> it.failures
                    else -> it.successes + it.failures
                }
            }
        }

        entries.apply {
            if (falconOne > 0) add(PieEntry(falconOne.toFloat(), "Falcon 1"))
            if (falconNine > 0) add(PieEntry(falconNine.toFloat(), "Falcon 9"))
            if (falconHeavy > 0) add(PieEntry(falconHeavy.toFloat(), "Falcon Heavy"))
        }

        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 3f
            setColors(colors)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "" + value.toInt()
                }
            }
        }

        val pieData = PieData(dataSet).apply {
            setValueTextColor(Color.WHITE)
            setValueTextSize(11f)
        }

        binding.launchHistoryPieChart.apply {
            this.centerText = context?.getString(R.string.pie_chart_title, "2006 - 2020")
                ?.generateCenterSpannableText()
            this.data = pieData
            if (data == true) animateY(1400, Easing.EaseInOutCubic) else invalidate()
        }
    }

    override fun setSuccessRate(stats: List<HistoryStatsModel>, animate: Boolean) {
        stats.forEach {
            when (it.rocket) {
                RocketType.FALCON_ONE -> {
                    animateProgress(animate, it.successRate, binding.falconOneRateProgress)

                    binding.falconOnePercentText.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                RocketType.FALCON_NINE -> {
                    animateProgress(animate, it.successRate, binding.falconNineRateProgress)

                    binding.falconNinePercentText.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                RocketType.FALCON_HEAVY -> {
                    animateProgress(animate, it.successRate, binding.falconHeavyRateProgress)

                    binding.falconHeavyPercentText.text =
                        context?.getString(R.string.percentage, it.successRate)
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

    override fun toggleFilterVisibility(filterVisible: Boolean) {
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

        this.filterVisible = filterVisible
    }

    override fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    override fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
    }

    override fun networkAvailable() {
        when(apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.getOrUpdate(null)
            ApiState.SUCCESS -> {}
        }
    }
}
