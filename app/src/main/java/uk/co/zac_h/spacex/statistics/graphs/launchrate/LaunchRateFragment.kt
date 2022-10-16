package uk.co.zac_h.spacex.statistics.graphs.launchrate

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
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchRateBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.RateStatsModel

class LaunchRateFragment : BaseFragment() {

    private lateinit var binding: FragmentLaunchRateBinding

    private val viewModel: LaunchRateViewModel by viewModels()

    private val navArgs: LaunchRateFragmentArgs by navArgs()

    private var statsList: List<RateStatsModel> = emptyList()

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
    ): View = FragmentLaunchRateBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.get()

        binding.launchRateConstraint.transitionName = getString(navArgs.type.title)

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
                            KeysModel("Falcon 1", stats.falconOne),
                            KeysModel("Falcon 9", stats.falconNine),
                            KeysModel("Falcon Heavy", stats.falconHeavy),
                            KeysModel("Failures", stats.failure),
                            KeysModel("Planned", stats.planned),
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

        viewModel.launchRate.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> response.data?.let {
                    update(false/*viewModel.cacheLocation != Repository.RequestLocation.CACHE*/, it)
                }
                is ApiResult.Failure -> showError(response.exception.message)
            }
        }
    }

    fun update(animate: Boolean, response: List<RateStatsModel>) {
        statsList = response

        var max = 0f

        val entries = response.map {
            val newMax = it.falconOne + it.falconNine + it.falconHeavy + it.failure + it.planned
            if (newMax > max) max = newMax

            BarEntry(
                it.year.toFloat(),
                floatArrayOf(it.falconOne, it.falconNine, it.falconHeavy, it.failure, it.planned)
            )
        }

        val set = BarDataSet(entries, "").apply {
            colors = listOf(
                ColorTemplate.rgb("29b6f6"), //F1
                ColorTemplate.rgb("9ccc65"), //F9
                ColorTemplate.rgb("ff7043"), //FH
                ColorTemplate.rgb("b00020"), //Failures
                ColorTemplate.rgb("66bb6a"), //Future
            )
            setDrawValues(false)

            stackLabels = arrayOf("Falcon 1", "Falcon 9", "Falcon Heavy", "Failures", "Future")
        }

        binding.statisticsBarChart.barChart.apply {
            if (animate) animateY(400, Easing.Linear)
            xAxis.labelCount = response.size
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = if ((max.toInt() % 2) == 0) max else max.plus(1)
                labelCount =
                    if ((max.toInt() % 2) == 0) max.toInt() / 2 else max.toInt().plus(1) / 2
            }
            data = BarData(listOf(set))
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
