package uk.co.zac_h.spacex.statistics.graphs.launchmass

import android.os.Bundle
import android.view.*
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
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchMassBinding
import uk.co.zac_h.spacex.statistics.adapters.Statistics
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel

class LaunchMassFragment : BaseFragment() {

    override val title by lazy { getString(Statistics.MASS_TO_ORBIT.title) }

    private lateinit var binding: FragmentLaunchMassBinding

    private val viewModel: LaunchMassViewModel by viewModels()

    private val navArgs: LaunchMassFragmentArgs by navArgs()

    private var statsList: List<LaunchMassStatsModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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

        binding.launchMassFilterTint.setOnClickListener {
            toggleFilterVisibility(false)
        }

        binding.launchMassRocketChipGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.filterRocket = when (checkedId) {
                binding.launchMassFalconOneToggle.id -> RocketType.FALCON_ONE
                binding.launchMassFalconNineToggle.id -> RocketType.FALCON_NINE
                binding.launchMassFalconHeavyToggle.id -> RocketType.FALCON_HEAVY
                else -> null
            }
            viewModel.get()
        }

        binding.launchMassTypeChipGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.filterType = when (checkedId) {
                binding.launchMassRocketToggle.id -> LaunchMassViewType.ROCKETS
                binding.launchMassOrbitToggle.id -> LaunchMassViewType.ORBIT
                else -> null
            }
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
            when (response.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> response.data?.let {
                    update(viewModel.cacheLocation != Repository.RequestLocation.CACHE, it)
                }
                ApiResult.Status.FAILURE -> showError(response.error?.message)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        toggleFilterVisibility(viewModel.filterState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filter -> {
            binding.statisticsBarChart.key.visibility = View.GONE
            binding.statisticsBarChart.barChart.apply {
                onTouchListener.setLastHighlighted(null)
                highlightValues(null)
            }
            toggleFilterVisibility(!viewModel.filterState)
            true
        }
        R.id.reload -> {
            viewModel.get(CachePolicy.REFRESH)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun update(data: Any, response: List<LaunchMassStatsModel>) {
        hideProgress()
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
                                it.falconOne.LEO + it.falconNine.LEO + it.falconHeavy.LEO,
                                it.falconOne.GTO + it.falconNine.GTO + it.falconHeavy.GTO,
                                it.falconOne.SSO + it.falconNine.SSO + it.falconHeavy.SSO,
                                it.falconOne.ISS + it.falconNine.ISS + it.falconHeavy.ISS,
                                it.falconOne.HCO + it.falconNine.HCO + it.falconHeavy.HCO,
                                it.falconOne.MEO + it.falconNine.MEO + it.falconHeavy.MEO,
                                it.falconOne.SO + it.falconNine.SO + it.falconHeavy.SO,
                                it.falconOne.ED_L1 + it.falconNine.ED_L1 + it.falconHeavy.ED_L1,
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
            0f.add(f1?.LEO, f9?.LEO, fh?.LEO).let {
                if (it > 0) KeysModel("LEO", it) else null
            },
            0f.add(f1?.GTO, f9?.GTO, fh?.GTO).let {
                if (it > 0) KeysModel("GTO", it) else null
            },
            0f.add(f1?.SSO, f9?.SSO, fh?.SSO).let {
                if (it > 0) KeysModel("SSO", it) else null
            },
            0f.add(f1?.ISS, f9?.ISS, fh?.ISS).let {
                if (it > 0) KeysModel("ISS", it) else null
            },
            0f.add(f1?.HCO, f9?.HCO, fh?.HCO).let {
                if (it > 0) KeysModel("HCO", it) else null
            },
            0f.add(f1?.MEO, f9?.MEO, fh?.MEO).let {
                if (it > 0) KeysModel("MEO", it) else null
            },
            0f.add(f1?.SO, f9?.SO, fh?.SO).let {
                if (it > 0) KeysModel("SO", it) else null
            },
            0f.add(f1?.ED_L1, f9?.ED_L1, fh?.ED_L1).let {
                if (it > 0) KeysModel("ED-L1", it) else null
            },
            0f.add(f1?.other, f9?.other, fh?.other).let {
                if (it > 0) KeysModel("Other", it) else null
            },
            KeysModel("Total", 0f.add(f1?.total, f9?.total, fh?.total))
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
            KeysModel("Total", 0f.add(f1?.total, f9?.total, fh?.total))
        )

    private fun orbitsToArray(orbitMassModel: OrbitMassModel): FloatArray = floatArrayOf(
        orbitMassModel.LEO,
        orbitMassModel.GTO,
        orbitMassModel.SSO,
        orbitMassModel.ISS,
        orbitMassModel.HCO,
        orbitMassModel.MEO,
        orbitMassModel.SO,
        orbitMassModel.ED_L1,
        orbitMassModel.other
    )

    private fun toggleFilterVisibility(filterVisible: Boolean) {
        binding.launchMassFilterConstraint.apply {
            when (filterVisible) {
                true -> {
                    visibility = View.VISIBLE
                    startAnimation(animateEnterFromTop(context))
                }
                false -> {
                    visibility = View.GONE
                    startAnimation(animateExitToTop(context))
                }
            }
        }

        binding.launchMassFilterTint.apply {
            when (filterVisible) {
                true -> {
                    visibility = View.VISIBLE
                    startAnimation(animateFadeIn(context))
                }
                false -> {
                    visibility = View.GONE
                    startAnimation(animateFadeOut(context))
                }
            }
        }

        viewModel.showFilter(filterVisible)
    }

    fun showProgress() {

    }

    fun hideProgress() {

    }

    fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.get()
    }
}