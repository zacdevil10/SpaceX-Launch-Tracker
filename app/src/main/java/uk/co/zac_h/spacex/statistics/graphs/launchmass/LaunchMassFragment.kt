package uk.co.zac_h.spacex.statistics.graphs.launchmass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
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
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.types.LaunchMassViewType
import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.core.common.utils.sum
import uk.co.zac_h.spacex.databinding.FragmentLaunchMassBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.statistics.graphs.launchmass.filter.LaunchMassFilterViewModel
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel

class LaunchMassFragment : BaseFragment() {

    private lateinit var binding: FragmentLaunchMassBinding

    private val viewModel: LaunchMassViewModel by viewModels()
    private val filterViewModel: LaunchMassFilterViewModel by activityViewModels()

    private val navArgs: LaunchMassFragmentArgs by navArgs()

    private var statsList: List<LaunchMassStatsModel> = emptyList()

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
    ): View = FragmentLaunchMassBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.get()

        binding.launchMassConstraint.transitionName = getString(navArgs.type.title)

        filterViewModel.filterRocket.observe(viewLifecycleOwner) {
            viewModel.setRocketFilter(it)
            viewModel.get()
        }

        filterViewModel.filterType.observe(viewLifecycleOwner) {
            viewModel.setTypeFilter(it)
            viewModel.get()
        }

        val keyAdapter = StatisticsKeyAdapter(requireContext(), true)

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

                        binding.statisticsBarChart.key.visibility = View.VISIBLE

                        binding.statisticsBarChart.year.text = stats.year.toString()

                        val keys: List<KeysModel> = when (viewModel.filterType) {
                            LaunchMassViewType.ROCKETS -> {
                                when (viewModel.filterRocket) {
                                    RocketType.FALCON_ONE -> populateRocketKey(f1 = stats.falconOne)
                                    RocketType.FALCON_NINE -> populateRocketKey(f9 = stats.falconNine)
                                    RocketType.FALCON_HEAVY -> populateRocketKey(fh = stats.falconHeavy)
                                    else -> populateRocketKey(
                                        stats.falconOne,
                                        stats.falconNine,
                                        stats.falconHeavy
                                    )
                                }
                            }
                            LaunchMassViewType.ORBIT -> {
                                when (viewModel.filterRocket) {
                                    RocketType.FALCON_ONE -> populateOrbitKey(f1 = stats.falconOne)
                                    RocketType.FALCON_NINE -> populateOrbitKey(f9 = stats.falconNine)
                                    RocketType.FALCON_HEAVY -> populateOrbitKey(fh = stats.falconHeavy)
                                    else -> populateOrbitKey(
                                        stats.falconOne,
                                        stats.falconNine,
                                        stats.falconHeavy
                                    )
                                }
                            }
                            else -> emptyList()
                        }

                        keyAdapter.submitList(keys)
                    }
                }

                override fun onNothingSelected() {
                    binding.statisticsBarChart.key.visibility = View.GONE
                    keyAdapter.submitList(emptyList())
                }
            })
        }

        viewModel.launchMass.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> update(false/*viewModel.cacheLocation != Repository.RequestLocation.CACHE*/,
                    response.result
                )
                is ApiResult.Failure -> showError(response.exception.message)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        filterViewModel.clear()
    }

    private fun update(data: Any, response: List<LaunchMassStatsModel>) {
        statsList = response

        binding.statisticsBarChart.key.visibility = View.GONE

        val colors = ArrayList<Int>()

        if (viewModel.filterType == LaunchMassViewType.ORBIT) {
            colors.add(ColorTemplate.rgb("29b6f6"))
            colors.add(ColorTemplate.rgb("9ccc65"))
            colors.add(ColorTemplate.rgb("ff7043"))
            colors.add(ColorTemplate.rgb("8e24aa"))
            colors.add(ColorTemplate.rgb("3949ab"))
            colors.add(ColorTemplate.rgb("00897b"))
            colors.add(ColorTemplate.rgb("43a047"))
            colors.add(ColorTemplate.rgb("fdd835"))
            colors.add(ColorTemplate.rgb("6d4c41"))
        } else {
            when (viewModel.filterRocket) {
                RocketType.FALCON_ONE -> colors.add(ColorTemplate.rgb("29b6f6"))
                RocketType.FALCON_NINE -> colors.add(ColorTemplate.rgb("9ccc65"))
                RocketType.FALCON_HEAVY -> colors.add(ColorTemplate.rgb("ff7043"))
                else -> {
                    colors.add(ColorTemplate.rgb("29b6f6"))
                    colors.add(ColorTemplate.rgb("9ccc65"))
                    colors.add(ColorTemplate.rgb("ff7043"))
                }
            }
        }

        val entries = ArrayList<BarEntry>()

        var max = 0f
        var c = 0

        response.forEach {
            val newMax = when (viewModel.filterRocket) {
                RocketType.FALCON_ONE -> it.falconOne.total
                RocketType.FALCON_NINE -> it.falconNine.total
                RocketType.FALCON_HEAVY -> it.falconHeavy.total
                else -> it.falconOne.total + it.falconNine.total + it.falconHeavy.total
            }

            if (newMax == 0f) return@forEach

            if (newMax > max) max = newMax

            c++

            entries.add(
                BarEntry(
                    it.year.toFloat(),
                    when (viewModel.filterType) {
                        LaunchMassViewType.ROCKETS -> when (viewModel.filterRocket) {
                            RocketType.FALCON_ONE -> floatArrayOf(it.falconOne.total)
                            RocketType.FALCON_NINE -> floatArrayOf(it.falconNine.total)
                            RocketType.FALCON_HEAVY -> floatArrayOf(it.falconHeavy.total)
                            else -> floatArrayOf(
                                it.falconOne.total,
                                it.falconNine.total,
                                it.falconHeavy.total
                            )
                        }
                        LaunchMassViewType.ORBIT -> when (viewModel.filterRocket) {
                            RocketType.FALCON_ONE -> orbitsToArray(it.falconOne)
                            RocketType.FALCON_NINE -> orbitsToArray(it.falconNine)
                            RocketType.FALCON_HEAVY -> orbitsToArray(it.falconHeavy)
                            else -> floatArrayOf(
                                it.falconOne.leo + it.falconNine.leo + it.falconHeavy.leo,
                                it.falconOne.gto + it.falconNine.gto + it.falconHeavy.gto,
                                it.falconOne.sso + it.falconNine.sso + it.falconHeavy.sso,
                                it.falconOne.iss + it.falconNine.iss + it.falconHeavy.iss,
                                it.falconOne.hco + it.falconNine.hco + it.falconHeavy.hco,
                                it.falconOne.meo + it.falconNine.meo + it.falconHeavy.meo,
                                it.falconOne.so + it.falconNine.so + it.falconHeavy.so,
                                it.falconOne.edL1 + it.falconNine.edL1 + it.falconHeavy.edL1,
                                it.falconOne.other + it.falconNine.other + it.falconHeavy.other
                            )
                        }
                        else -> floatArrayOf(
                            it.falconOne.total,
                            it.falconNine.total,
                            it.falconHeavy.total
                        )
                    }
                )
            )
        }

        val set = BarDataSet(entries, "").apply {
            setColors(colors)
            setDrawValues(false)

            stackLabels = when (viewModel.filterType) {
                LaunchMassViewType.ROCKETS -> when (viewModel.filterRocket) {
                    RocketType.FALCON_ONE -> arrayOf("Falcon 1")
                    RocketType.FALCON_NINE -> arrayOf("Falcon 9")
                    RocketType.FALCON_HEAVY -> arrayOf("Falcon Heavy")
                    else -> arrayOf("Falcon 1", "Falcon 9", "Falcon Heavy")
                }
                LaunchMassViewType.ORBIT -> arrayOf(
                    "LEO",
                    "GTO",
                    "SSO",
                    "ISS",
                    "HCO",
                    "MEO",
                    "SO",
                    "ED-L1",
                    "Other"
                )
                else -> arrayOf("")
            }
        }

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set)

        binding.statisticsBarChart.barChart.apply {
            onTouchListener.setLastHighlighted(null)
            highlightValues(null)
            if (data == true) animateY(400, Easing.Linear)
            xAxis.labelCount = c
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

    fun populateOrbitKey(
        f1: OrbitMassModel? = null,
        f9: OrbitMassModel? = null,
        fh: OrbitMassModel? = null
    ): List<KeysModel> =
        listOfNotNull(
            sum(f1?.leo, f9?.leo, fh?.leo).let {
                if (it > 0) KeysModel("LEO", it) else null
            },
            sum(f1?.gto, f9?.gto, fh?.gto).let {
                if (it > 0) KeysModel("GTO", it) else null
            },
            sum(f1?.sso, f9?.sso, fh?.sso).let {
                if (it > 0) KeysModel("SSO", it) else null
            },
            sum(f1?.iss, f9?.iss, fh?.iss).let {
                if (it > 0) KeysModel("ISS", it) else null
            },
            sum(f1?.hco, f9?.hco, fh?.hco).let {
                if (it > 0) KeysModel("HCO", it) else null
            },
            sum(f1?.meo, f9?.meo, fh?.meo).let {
                if (it > 0) KeysModel("MEO", it) else null
            },
            sum(f1?.so, f9?.so, fh?.so).let {
                if (it > 0) KeysModel("SO", it) else null
            },
            sum(f1?.edL1, f9?.edL1, fh?.edL1).let {
                if (it > 0) KeysModel("ED-L1", it) else null
            },
            sum(f1?.other, f9?.other, fh?.other).let {
                if (it > 0) KeysModel("Other", it) else null
            },
            KeysModel("Total", sum(f1?.total, f9?.total, fh?.total))
        )

    fun populateRocketKey(
        f1: OrbitMassModel? = null,
        f9: OrbitMassModel? = null,
        fh: OrbitMassModel? = null
    ): List<KeysModel> =
        listOfNotNull(
            f1?.let { if (it.total > 0) KeysModel("Falcon 1", it.total) else null },
            f9?.let { if (it.total > 0) KeysModel("Falcon 9", it.total) else null },
            fh?.let { if (it.total > 0) KeysModel("Falcon Heavy", it.total) else null },
            KeysModel("Total", sum(f1?.total, f9?.total, fh?.total))
        )

    private fun orbitsToArray(orbitMassModel: OrbitMassModel): FloatArray = floatArrayOf(
        orbitMassModel.leo,
        orbitMassModel.gto,
        orbitMassModel.sso,
        orbitMassModel.iss,
        orbitMassModel.hco,
        orbitMassModel.meo,
        orbitMassModel.so,
        orbitMassModel.edL1,
        orbitMassModel.other
    )

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.get()
    }
}
