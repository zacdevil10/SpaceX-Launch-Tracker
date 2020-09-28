package uk.co.zac_h.spacex.statistics.graphs.launchrate

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchRateBinding
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.RateStatsModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchRateFragment : Fragment(), LaunchRateContract.LaunchRateView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchRateBinding? = null

    private var heading: String? = null

    private var presenter: LaunchRateContract.LaunchRatePresenter? = null

    private lateinit var statsList: ArrayList<RateStatsModel>

    private lateinit var keyAdapter: StatisticsKeyAdapter
    private var keys: ArrayList<KeysModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()

        heading = arguments?.getString("heading")
        statsList = savedInstanceState?.getParcelableArrayList("launches") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchRateBinding.inflate(inflater, container, false)
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

        binding?.launchRateConstraint?.transitionName = heading

        hideProgress()

        presenter = LaunchRatePresenterImpl(this, LaunchRateInteractorImpl())

        keyAdapter = StatisticsKeyAdapter(context, keys, false)

        binding?.launchRateKeyRecycler?.apply {
            layoutManager = LinearLayoutManager(this@LaunchRateFragment.context)
            adapter = keyAdapter
        }

        binding?.launchRateBarChart?.apply {
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

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val stats = statsList[(e.x - 2006).toInt()]

                        keys.clear()

                        binding?.apply {
                            launchRateKey.visibility = View.VISIBLE

                            launchRateYear.text = stats.year.toString()

                            keys.apply {
                                if (stats.falconOne > 0) {
                                    add(KeysModel("Falcon 1", stats.falconOne))
                                }
                                if (stats.falconNine > 0) {
                                    add(KeysModel("Falcon 9", stats.falconNine))
                                }
                                if (stats.falconHeavy > 0) {
                                    add(KeysModel("Falcon Heavy", stats.falconHeavy))
                                }
                                if (stats.failure > 0) {
                                    add(KeysModel("Failures", stats.failure))
                                }
                                if (stats.planned > 0) {
                                    add(KeysModel("Planned", stats.planned))
                                }
                                add(KeysModel("Total", e.y))
                            }
                        }

                        keyAdapter.notifyDataSetChanged()
                    }
                }

                override fun onNothingSelected() {
                    binding?.launchRateKey?.visibility = View.GONE

                    keys.clear()
                    keyAdapter.notifyDataSetChanged()
                }
            })
        }

        if (statsList.isEmpty()) {
            presenter?.getLaunchList()
        } else {
            presenter?.addLaunchList(statsList)
        }
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("launches", statsList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_pads, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            statsList.clear()
            presenter?.getLaunchList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateBarChart(stats: List<RateStatsModel>, animate: Boolean) {
        if (statsList.isEmpty()) statsList.addAll(stats)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6")) //F1
        colors.add(ColorTemplate.rgb("9ccc65")) //F9
        colors.add(ColorTemplate.rgb("ff7043")) //FH
        colors.add(ColorTemplate.rgb("b00020")) //Failures
        colors.add(ColorTemplate.rgb("66bb6a")) //Future

        val entries = ArrayList<BarEntry>()

        var max = 0f

        stats.forEach {
            val newMax = it.falconOne + it.falconNine + it.falconHeavy + it.failure + it.planned
            if (newMax > max) max = newMax
            entries.add(
                BarEntry(
                    it.year.toFloat(),
                    floatArrayOf(
                        it.falconOne,
                        it.falconNine,
                        it.falconHeavy,
                        it.failure,
                        it.planned
                    )
                )
            )
        }

        val set = BarDataSet(entries, "").apply {
            setColors(colors)
            setDrawValues(false)

            stackLabels = arrayOf("Falcon 1", "Falcon 9", "Falcon Heavy", "Failures", "Future")
        }

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set)

        binding?.launchRateBarChart?.apply {
            if (animate) animateY(400, Easing.Linear)
            xAxis.labelCount = stats.size
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = if ((max.toInt() % 2) == 0) max else max.plus(1)
                labelCount =
                    if ((max.toInt() % 2) == 0) max.toInt() / 2 else max.toInt().plus(1) / 2
            }
            data = BarData(dataSets)
            invalidate()
        }
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
                if (statsList.isEmpty() || it.progressIndicator.isShown) presenter?.getLaunchList()
            }
        }
    }
}
