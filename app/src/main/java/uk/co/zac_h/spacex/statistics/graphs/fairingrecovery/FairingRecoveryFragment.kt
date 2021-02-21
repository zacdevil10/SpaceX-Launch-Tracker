package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

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
import uk.co.zac_h.spacex.databinding.FragmentFairingRecoveryBinding
import uk.co.zac_h.spacex.statistics.adapters.Statistics
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel
import uk.co.zac_h.spacex.utils.models.KeysModel

class FairingRecoveryFragment : BaseFragment(), NetworkInterface.View<List<FairingRecoveryModel>> {

    override val title: String by lazy { Statistics.FAIRING_RECOVERY.title }

    private var _binding: FragmentFairingRecoveryBinding? = null
    private val binding get() = _binding!!

    private var presenter: NetworkInterface.Presenter<List<FairingRecoveryModel>?>? = null

    private lateinit var statsList: ArrayList<FairingRecoveryModel>
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
    ): View = FragmentFairingRecoveryBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
        binding.toolbarLayout.toolbar.setup()

        binding.fairingRecoveryConstraint.transitionName = heading

        presenter = FairingRecoveryPresenter(this, FairingRecoveryInteractor())

        keyAdapter = StatisticsKeyAdapter(context, keys, false)

        binding.statisticsBarChart.recycler.apply {
            layoutManager = LinearLayoutManager(this@FairingRecoveryFragment.context)
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
                                if (stats.successes > 0) add(
                                    KeysModel(
                                        "Successes",
                                        stats.successes
                                    )
                                )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    override fun update(data: Any, response: List<FairingRecoveryModel>) {
        apiState = ApiState.SUCCESS
        if (statsList.isEmpty()) statsList.addAll(response)

        val colors = ArrayList<Int>()

        colors.add(ColorTemplate.rgb("29b6f6")) //Success
        colors.add(ColorTemplate.rgb("b00020")) //Failure

        val entries = ArrayList<BarEntry>()

        var max = 0f

        response.forEach {
            val newMax = it.successes + it.failures
            if (newMax > max) max = newMax
            entries.add(
                BarEntry(
                    it.year.toFloat(),
                    floatArrayOf(
                        it.successes,
                        it.failures
                    )
                )
            )
        }

        val set = BarDataSet(entries, "").apply {
            setColors(colors)
            setDrawValues(false)

            stackLabels = arrayOf("Success", "Failure")
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
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.getOrUpdate(null)
            ApiState.SUCCESS -> {
            }
        }
    }
}