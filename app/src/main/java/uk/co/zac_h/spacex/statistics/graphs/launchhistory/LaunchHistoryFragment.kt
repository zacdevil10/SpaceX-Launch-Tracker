package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchHistoryBinding
import uk.co.zac_h.spacex.utils.LaunchHistoryFilter
import uk.co.zac_h.spacex.utils.RocketType
import uk.co.zac_h.spacex.utils.generateCenterSpannableText
import uk.co.zac_h.spacex.utils.models.HistoryStatsModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchHistoryFragment : Fragment(), LaunchHistoryContract.LaunchHistoryView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchHistoryBinding? = null

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
    ): View? {
        binding = FragmentLaunchHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.launchHistoryConstraint?.transitionName = heading

        hideProgress()

        presenter = LaunchHistoryPresenterImpl(this, LaunchHistoryInteractorImpl())

        binding?.launchHistoryChipGroup?.setOnCheckedChangeListener { _, checkedId ->
            filter = when (checkedId) {
                binding?.launchHistorySuccessToggle?.id -> LaunchHistoryFilter.SUCCESSES
                binding?.launchHistoryFailureToggle?.id -> LaunchHistoryFilter.FAILURES
                else -> null
            }
            presenter?.updateFilter(launchStats)
        }

        presenter?.apply {
            if (launchStats.isEmpty()) getLaunchList() else addLaunchList(launchStats)
        }

        //Pie chart appearance
        binding?.launchHistoryPieChart?.apply {
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
        outState.putBoolean("filter", filterVisible)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
        binding = null
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

    override fun updatePieChart(stats: List<HistoryStatsModel>, animate: Boolean) {
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

        val data = PieData(dataSet).apply {
            setValueTextColor(Color.WHITE)
            setValueTextSize(11f)
        }

        binding?.launchHistoryPieChart?.apply {
            if (animate) animateY(1400, Easing.EaseInOutCubic)
            this.centerText = context?.getString(R.string.pie_chart_title, "2006 - 2020")
                ?.generateCenterSpannableText()
            this.data = data
            invalidate()
        }
    }

    override fun setSuccessRate(stats: List<HistoryStatsModel>, animate: Boolean) {
        stats.forEach {
            when (it.rocket) {
                RocketType.FALCON_ONE -> {
                    if (animate) ValueAnimator.ofInt(0, it.successRate).apply {
                        duration = 1000
                        addUpdateListener { valueAnim ->
                            binding?.launchHistoryFalconOneRateProgress?.progress =
                                valueAnim.animatedValue as Int
                        }
                    }.start() else binding?.launchHistoryFalconOneRateProgress?.progress =
                        it.successRate

                    binding?.launchHistoryFalconOnePercentText?.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                RocketType.FALCON_NINE -> {
                    if (animate) ValueAnimator.ofInt(0, it.successRate).apply {
                        duration = 1000
                        addUpdateListener { valueAnim ->
                            binding?.launchHistoryFalconNineRateProgress?.progress =
                                valueAnim.animatedValue as Int
                        }
                    }.start() else binding?.launchHistoryFalconNineRateProgress?.progress =
                        it.successRate

                    binding?.launchHistoryFalconNinePercentText?.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
                RocketType.FALCON_HEAVY -> {
                    if (animate) ValueAnimator.ofInt(0, it.successRate).apply {
                        duration = 1000
                        addUpdateListener { valueAnim ->
                            binding?.launchHistoryFalconHeavyRateProgress?.progress =
                                valueAnim.animatedValue as Int
                        }
                    }.start() else binding?.launchHistoryFalconHeavyRateProgress?.progress =
                        it.successRate

                    binding?.launchHistoryFalconHeavyPercentText?.text =
                        context?.getString(R.string.percentage, it.successRate)
                }
            }
        }
    }

    override fun toggleFilterVisibility(filterVisible: Boolean) {
        binding?.launchHistoryFilterConstraint?.visibility = when (filterVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        this.filterVisible = filterVisible
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (launchStats.isEmpty() || it.progressIndicator.isShown) presenter?.getLaunchList()
            }
        }
    }
}
