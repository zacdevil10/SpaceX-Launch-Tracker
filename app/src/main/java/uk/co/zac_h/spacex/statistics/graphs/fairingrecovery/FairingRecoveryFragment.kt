package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
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
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentFairingRecoveryBinding
import uk.co.zac_h.spacex.statistics.adapters.Statistics
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel
import uk.co.zac_h.spacex.utils.models.KeysModel

class FairingRecoveryFragment : BaseFragment() {

    override val title by lazy { getString(Statistics.FAIRING_RECOVERY.title) }

    private lateinit var binding: FragmentFairingRecoveryBinding

    private val viewModel: FairingRecoveryViewModel by viewModels()

    private val navArgs: FairingRecoveryFragmentArgs by navArgs()

    private var statsList: List<FairingRecoveryModel> = ArrayList()
    private lateinit var keyAdapter: StatisticsKeyAdapter
    private var keys: ArrayList<KeysModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentFairingRecoveryBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.get()

        binding.toolbarLayout.toolbar.apply {
            setup()
            createOptionsMenu(R.menu.menu_statistics_reload)
        }

        binding.fairingRecoveryConstraint.transitionName = getString(navArgs.type.title)

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

        viewModel.fairingRecovery.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> response.data?.let {
                    update(viewModel.cacheLocation != Repository.RequestLocation.CACHE, it)
                }
                ApiResult.Status.FAILURE -> showError(response.error?.message)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            viewModel.get(CachePolicy.REFRESH)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun update(data: Boolean, response: List<FairingRecoveryModel>) {
        statsList = response

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
            if (data) animateY(400, Easing.Linear)
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

    fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.get()
    }
}