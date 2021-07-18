package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import android.os.Bundle
import android.view.*
import androidx.core.view.doOnPreDraw
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
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLandingHistoryBinding
import uk.co.zac_h.spacex.statistics.adapters.Statistics
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.LandingHistoryModel

class LandingHistoryFragment : BaseFragment(), NetworkInterface.View<List<LandingHistoryModel>> {

    override val title: String by lazy { Statistics.LANDING_HISTORY.title }

    private lateinit var binding: FragmentLandingHistoryBinding

    private var presenter: NetworkInterface.Presenter<List<LandingHistoryModel>?>? = null

    private lateinit var statsList: ArrayList<LandingHistoryModel>
    private lateinit var keyAdapter: StatisticsKeyAdapter
    private var keys: ArrayList<KeysModel> = ArrayList()

    private var heading: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()

        heading = arguments?.getString("heading")
        statsList = savedInstanceState?.getParcelableArrayList("stats") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLandingHistoryBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
        binding.toolbarLayout.toolbar.setup()

        binding.landingHistoryConstraint.transitionName = heading

        presenter = LandingHistoryPresenter(this, LandingHistoryInteractor())

        keyAdapter = StatisticsKeyAdapter(requireContext(), keys, false)

        binding.statisticsBarChart.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = keyAdapter
        }

        binding.statisticsBarChart.barChart.apply {
            setup()

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val stats = statsList.filter { it.year == e.x.toInt() }[0]

                        keys.clear()

                        binding.apply {
                            statisticsBarChart.key.visibility = View.VISIBLE

                            statisticsBarChart.year.text = stats.year.toString()

                            keys.apply {
                                if (stats.ocean > 0) add(KeysModel("Ocean", stats.ocean))
                                if (stats.rtls > 0) add(KeysModel("RTLS", stats.rtls))
                                if (stats.asds > 0) add(KeysModel("ASDS", stats.asds))
                                if (stats.failures > 0) add(KeysModel("Failures", stats.failures))
                                add(KeysModel("Total", e.y))
                            }
                        }

                        keyAdapter.notifyDataSetChanged()
                    }
                }

                override fun onNothingSelected() {
                    binding.statisticsBarChart.key.visibility = View.GONE
                    keys.clear()
                    keyAdapter.notifyDataSetChanged()
                }
            })
        }

        presenter?.getOrUpdate(statsList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("stats", statsList)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_reload, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            apiState = ApiState.PENDING
            statsList.clear()
            presenter?.getOrUpdate(null)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(data: Any, response: List<LandingHistoryModel>) {
        apiState = ApiState.SUCCESS
        if (statsList.isEmpty()) statsList.addAll(response)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6")) //Ocean
        colors.add(ColorTemplate.rgb("9ccc65")) //RTLS
        colors.add(ColorTemplate.rgb("ff7043")) //ASDS
        colors.add(ColorTemplate.rgb("b00020")) //Failures

        val entries = ArrayList<BarEntry>()

        var max = 0f

        response.forEach {
            val newMax = it.ocean + it.asds + it.rtls + it.failures
            if (newMax > max) max = newMax
            entries.add(
                BarEntry(
                    it.year.toFloat(),
                    floatArrayOf(
                        it.ocean,
                        it.rtls,
                        it.asds,
                        it.failures
                    )
                )
            )
        }

        val set = BarDataSet(entries, "").apply {
            setColors(colors)
            setDrawValues(false)

            stackLabels = arrayOf("Ocean", "RTLS", "ASDS", "Failures")
        }

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set)

        binding.statisticsBarChart.barChart.apply {
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