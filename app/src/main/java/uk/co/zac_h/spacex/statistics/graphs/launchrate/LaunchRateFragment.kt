package uk.co.zac_h.spacex.statistics.graphs.launchrate

import android.os.Bundle
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchRateBinding
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.RateStatsModel

class LaunchRateFragment : BaseFragment(), NetworkInterface.View<List<RateStatsModel>> {

    override var title: String = "Launch Rate"

    private var binding: FragmentLaunchRateBinding? = null

    private var heading: String? = null

    private var presenter: NetworkInterface.Presenter<List<RateStatsModel>?>? = null

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
    ): View = FragmentLaunchRateBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        binding?.launchRateConstraint?.transitionName = heading

        hideProgress()

        presenter = LaunchRatePresenterImpl(this, LaunchRateInteractorImpl())

        keyAdapter = StatisticsKeyAdapter(context, keys, false)

        binding?.statisticsBarChart?.recycler?.apply {
            layoutManager = LinearLayoutManager(this@LaunchRateFragment.context)
            adapter = keyAdapter
        }

        binding?.statisticsBarChart?.barChart?.apply {
            setup()

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val stats = statsList.filter { it.year == e.x.toInt() }[0]

                        keys.clear()

                        binding?.apply {
                            statisticsBarChart.key.visibility = View.VISIBLE

                            statisticsBarChart.year.text = stats.year.toString()

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
                    binding?.statisticsBarChart?.key?.visibility = View.GONE

                    keys.clear()
                    keyAdapter.notifyDataSetChanged()
                }
            })
        }

        presenter?.getOrUpdate(statsList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("launches", statsList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_reload, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            statsList.clear()
            presenter?.getOrUpdate(null)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(data: Any, response: List<RateStatsModel>) {
        if (statsList.isEmpty()) statsList.addAll(response)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6")) //F1
        colors.add(ColorTemplate.rgb("9ccc65")) //F9
        colors.add(ColorTemplate.rgb("ff7043")) //FH
        colors.add(ColorTemplate.rgb("b00020")) //Failures
        colors.add(ColorTemplate.rgb("66bb6a")) //Future

        val entries = ArrayList<BarEntry>()

        var max = 0f

        response.forEach {
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

        binding?.statisticsBarChart?.barChart?.apply {
            if (data == true) animateY(400, Easing.Linear)
            xAxis.labelCount = response.size
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = if ((max.toInt() % 2) == 0) max else max.plus(1)
                labelCount =
                    if ((max.toInt() % 2) == 0) max.toInt() / 2 else max.toInt().plus(1) / 2
            }
            this.data = BarData(dataSets)
            invalidate()
        }
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (statsList.isEmpty() || it.progressIndicator.isShown) presenter?.getOrUpdate(null)
            }
        }
    }
}
