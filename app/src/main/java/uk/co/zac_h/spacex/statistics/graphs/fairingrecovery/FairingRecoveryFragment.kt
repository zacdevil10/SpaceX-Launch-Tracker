package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

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
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentFairingRecoveryBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.models.FairingRecoveryModel
import uk.co.zac_h.spacex.utils.models.KeysModel

class FairingRecoveryFragment : BaseFragment() {

    private lateinit var binding: FragmentFairingRecoveryBinding

    private val viewModel: FairingRecoveryViewModel by viewModels()

    private val navArgs: FairingRecoveryFragmentArgs by navArgs()

    private var statsList: List<FairingRecoveryModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
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

        binding.fairingRecoveryConstraint.transitionName = getString(navArgs.type.title)

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

                        val keys = listOf(
                            KeysModel("Successes", stats.successes),
                            KeysModel("Failures", stats.failures),
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

        viewModel.fairingRecovery.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> update(false/*viewModel.cacheLocation != Repository.RequestLocation.CACHE*/,
                    response.result
                )
                is ApiResult.Failure -> showError(response.exception.message)
            }
        }
    }

    private fun update(data: Boolean, response: List<FairingRecoveryModel>) {
        statsList = response

        var max = 0f

        val entries = response.map {
            val newMax = it.successes + it.failures
            if (newMax > max) max = newMax
            BarEntry(it.year.toFloat(), floatArrayOf(it.successes, it.failures))
        }

        val set = BarDataSet(entries, "").apply {
            colors = listOf(
                ColorTemplate.rgb("29b6f6"), //Success
                ColorTemplate.rgb("b00020")  //Failure
            )
            setDrawValues(false)

            stackLabels = arrayOf("Success", "Failure")
        }

        binding.statisticsBarChart.barChart.apply {
            if (data) animateY(400, Easing.Linear)
            xAxis.labelCount = response.size
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = if ((max.toInt() % 2) == 0) max else max.plus(1)
                labelCount = if ((max.toInt() % 2) == 0) {
                    max.toInt() / 2
                } else max.toInt().plus(1) / 2
            }
            this.data = BarData(listOf(set))
            invalidate()
        }
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.get()
    }
}
