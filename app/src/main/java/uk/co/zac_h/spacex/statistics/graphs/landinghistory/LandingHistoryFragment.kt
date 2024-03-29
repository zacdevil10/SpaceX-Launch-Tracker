package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import android.os.Bundle
import android.view.LayoutInflater
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLandingHistoryBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.LandingHistoryModel

class LandingHistoryFragment : BaseFragment() {

    private lateinit var binding: FragmentLandingHistoryBinding

    private val viewModel: LandingHistoryViewModel by viewModels()

    private val navArgs: LandingHistoryFragmentArgs by navArgs()

    private var statsList: List<LandingHistoryModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host
        }

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
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

        viewModel.get()

        binding.landingHistoryConstraint.transitionName = getString(navArgs.type.title)

        val keyAdapter = StatisticsKeyAdapter(requireContext(), false)

        binding.statisticsBarChart.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = keyAdapter
        }

        binding.statisticsBarChart.barChart.apply {
            setup()

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val stats = statsList.first { it.year == e.x.toInt() }

                        binding.statisticsBarChart.apply {
                            key.visibility = View.VISIBLE
                            year.text = stats.year.toString()
                        }

                        val keys = listOfNotNull(
                            if (stats.ocean > 0) KeysModel("Ocean", stats.ocean) else null,
                            if (stats.rtls > 0) KeysModel("RTLS", stats.rtls) else null,
                            if (stats.asds > 0) KeysModel("ASDS", stats.asds) else null,
                            if (stats.failures > 0) KeysModel("Failures", stats.failures) else null,
                            KeysModel("Total", e.y)
                        )

                        keyAdapter.submitList(keys)
                    }
                }

                override fun onNothingSelected() {
                    binding.statisticsBarChart.key.visibility = View.GONE
                    keyAdapter.submitList(emptyList())
                }
            })
        }

        viewModel.landingHistory.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> response.data?.let {
                    update(false/*viewModel.cacheLocation != Repository.RequestLocation.CACHE*/, it)
                }
                is ApiResult.Failure -> showError(response.exception.message)
            }
        }
    }

    fun update(data: Any, response: List<LandingHistoryModel>) {
        statsList = response

        var max = 0f

        val entries = response.map {
            val newMax = it.ocean + it.asds + it.rtls + it.failures
            if (newMax > max) max = newMax
            BarEntry(it.year.toFloat(), floatArrayOf(it.ocean, it.rtls, it.asds, it.failures))
        }

        val set = BarDataSet(entries, "").apply {
            colors = listOf(
                ColorTemplate.rgb("29b6f6"), //Ocean
                ColorTemplate.rgb("9ccc65"), //RTLS
                ColorTemplate.rgb("ff7043"), //ASDS
                ColorTemplate.rgb("b00020"), //Failures
            )
            setDrawValues(false)

            stackLabels = arrayOf("Ocean", "RTLS", "ASDS", "Failures")
        }

        binding.statisticsBarChart.barChart.apply {
            if (data == true) animateY(400, Easing.Linear)
            xAxis.labelCount = response.size
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = if ((max.toInt() % 2) == 0) max else max.plus(1)
                labelCount =
                    if ((max.toInt() % 2) == 0) max.toInt() / 2 else max.toInt().plus(1) / 2
            }
            this.data = BarData(listOf(set))
            invalidate()
        }
    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.get()
    }
}
