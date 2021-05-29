package uk.co.zac_h.spacex.statistics.graphs.launchmass

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
import uk.co.zac_h.spacex.databinding.FragmentLaunchMassBinding
import uk.co.zac_h.spacex.statistics.adapters.Statistics
import uk.co.zac_h.spacex.statistics.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.utils.*
import uk.co.zac_h.spacex.utils.models.KeysModel
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel

class LaunchMassFragment : BaseFragment(), LaunchMassContract.View {

    override val title: String by lazy { Statistics.MASS_TO_ORBIT.title }

    private lateinit var binding: FragmentLaunchMassBinding

    private var presenter: LaunchMassContract.Presenter? = null

    private var filterVisible = false
    private var filterRocket: RocketType? = null
    private var filterType: LaunchMassViewType? = LaunchMassViewType.ROCKETS

    private var heading: String? = null
    private lateinit var statsList: ArrayList<LaunchMassStatsModel>

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchMassBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding.toolbarLayout.toolbar)

        binding.toolbarLayout.toolbar.setup()

        binding.launchMassConstraint.transitionName = heading

        hideProgress()

        binding.launchMassFilterTint.setOnClickListener {
            showFilter(false)
        }

        presenter = LaunchMassPresenter(this, LaunchMassInteractor())

        binding.launchMassRocketChipGroup.setOnCheckedChangeListener { _, checkedId ->
            filterRocket = when (checkedId) {
                binding.launchMassFalconOneToggle.id -> RocketType.FALCON_ONE
                binding.launchMassFalconNineToggle.id -> RocketType.FALCON_NINE
                binding.launchMassFalconHeavyToggle.id -> RocketType.FALCON_HEAVY
                else -> null
            }
            presenter?.updateFilter(statsList)
        }

        binding.launchMassTypeChipGroup.setOnCheckedChangeListener { _, checkedId ->
            filterType = when (checkedId) {
                binding.launchMassRocketToggle.id -> LaunchMassViewType.ROCKETS
                binding.launchMassOrbitToggle.id -> LaunchMassViewType.ORBIT
                else -> null
            }
            presenter?.updateFilter(statsList)
        }

        keyAdapter = StatisticsKeyAdapter(context, keys, true)

        binding.statisticsBarChart.recycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchMassFragment.context)
            adapter = keyAdapter
        }

        binding.statisticsBarChart.barChart.apply {
            setup()

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val stats = statsList.filter { it.year == e.x.toInt() }[0]

                        keys.clear()

                        binding.statisticsBarChart.key.visibility = View.VISIBLE

                        binding.statisticsBarChart.year.text = stats.year.toString()

                        when (filterType) {
                            LaunchMassViewType.ROCKETS -> {
                                when (filterRocket) {
                                    RocketType.FALCON_ONE -> presenter?.populateRocketKey(f1 = stats.falconOne)
                                    RocketType.FALCON_NINE -> presenter?.populateRocketKey(f9 = stats.falconNine)
                                    RocketType.FALCON_HEAVY -> presenter?.populateRocketKey(fh = stats.falconHeavy)
                                    else -> presenter?.populateRocketKey(
                                        stats.falconOne,
                                        stats.falconNine,
                                        stats.falconHeavy
                                    )
                                }
                            }
                            LaunchMassViewType.ORBIT -> {
                                when (filterRocket) {
                                    RocketType.FALCON_ONE -> presenter?.populateOrbitKey(f1 = stats.falconOne)
                                    RocketType.FALCON_NINE -> presenter?.populateOrbitKey(f9 = stats.falconNine)
                                    RocketType.FALCON_HEAVY -> presenter?.populateOrbitKey(fh = stats.falconHeavy)
                                    else -> presenter?.populateOrbitKey(
                                        stats.falconOne,
                                        stats.falconNine,
                                        stats.falconHeavy
                                    )
                                }
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
        outState.putParcelableArrayList("launches", statsList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filter -> {
            binding.statisticsBarChart.key.visibility = View.GONE
            binding.statisticsBarChart.barChart.apply {
                onTouchListener.setLastHighlighted(null)
                highlightValues(null)
            }
            presenter?.showFilter(!filterVisible)
            true
        }
        R.id.reload -> {
            apiState = ApiState.PENDING
            statsList.clear()
            presenter?.getOrUpdate(null)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(data: Any, response: List<LaunchMassStatsModel>) {
        apiState = ApiState.SUCCESS
        if (statsList.isEmpty()) statsList.addAll(response)

        binding.statisticsBarChart.key.visibility = View.GONE

        val colors = ArrayList<Int>()

        if (filterType == LaunchMassViewType.ORBIT) {
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
            when (filterRocket) {
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
            val newMax = when (filterRocket) {
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
                    when (filterType) {
                        LaunchMassViewType.ROCKETS -> when (filterRocket) {
                            RocketType.FALCON_ONE -> floatArrayOf(it.falconOne.total)
                            RocketType.FALCON_NINE -> floatArrayOf(it.falconNine.total)
                            RocketType.FALCON_HEAVY -> floatArrayOf(it.falconHeavy.total)
                            else -> floatArrayOf(
                                it.falconOne.total,
                                it.falconNine.total,
                                it.falconHeavy.total
                            )
                        }
                        LaunchMassViewType.ORBIT -> when (filterRocket) {
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

            stackLabels = when (filterType) {
                LaunchMassViewType.ROCKETS -> when (filterRocket) {
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

    override fun updateKey(keys: ArrayList<KeysModel>) {
        this.keys.addAll(keys)
    }

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

    override fun showFilter(filterVisible: Boolean) {
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

        this.filterVisible = filterVisible
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