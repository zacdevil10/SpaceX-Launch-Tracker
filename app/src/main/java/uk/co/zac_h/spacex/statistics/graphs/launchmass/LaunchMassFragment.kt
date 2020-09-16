package uk.co.zac_h.spacex.statistics.graphs.launchmass

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchMassBinding
import uk.co.zac_h.spacex.utils.LaunchMassViewType
import uk.co.zac_h.spacex.utils.RocketType
import uk.co.zac_h.spacex.utils.models.LaunchMassStatsModel
import uk.co.zac_h.spacex.utils.models.OrbitMassModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchMassFragment : Fragment(), LaunchMassContract.View,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchMassBinding? = null

    private var presenter: LaunchMassContract.Presenter? = null

    private var filterVisible = false
    private var filterRocket: RocketType? = null
    private var filterType: LaunchMassViewType? = LaunchMassViewType.ROCKETS

    private lateinit var statsList: ArrayList<LaunchMassStatsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        statsList = savedInstanceState?.getParcelableArrayList("launches") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchMassBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        hideProgress()

        binding?.launchMassFilterTint?.setOnClickListener {
            showFilter(false)
        }

        presenter = LaunchMassPresenter(this, LaunchMassInteractor())

        if (statsList.isEmpty()) {
            presenter?.getLaunchList()
        } else {
            presenter?.addLaunchList(statsList)
        }

        binding?.launchMassRocketChipGroup?.setOnCheckedChangeListener { _, checkedId ->
            filterRocket = when (checkedId) {
                binding?.launchMassFalconOneToggle?.id -> RocketType.FALCON_ONE
                binding?.launchMassFalconNineToggle?.id -> RocketType.FALCON_NINE
                binding?.launchMassFalconHeavyToggle?.id -> RocketType.FALCON_HEAVY
                else -> null
            }
            presenter?.updateFilter(statsList)
        }

        binding?.launchMassTypeChipGroup?.setOnCheckedChangeListener { _, checkedId ->
            filterType = when (checkedId) {
                binding?.launchMassRocketToggle?.id -> LaunchMassViewType.ROCKETS
                binding?.launchMassOrbitToggle?.id -> LaunchMassViewType.ORBIT
                else -> null
            }
            presenter?.updateFilter(statsList)
        }

        binding?.launchMassBarChart?.apply {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "'" + value.toInt().toString().takeLast(2)
                    }
                }
                setDrawGridLines(false)
            }
            axisLeft.apply {
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
                isGranularityEnabled = true
                granularity = 1f
                axisMinimum = 0f
                setDrawGridLines(false)
            }
            axisRight.isEnabled = false
            setScaleEnabled(false)
            description.isEnabled = false
            setDrawBorders(false)
            isHighlightFullBarEnabled = true
            legend.apply {
                textColor = ContextCompat.getColor(context, R.color.color_on_background)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
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
        inflater.inflate(R.menu.menu_statistics, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filter -> {
            presenter?.showFilter(!filterVisible)
            true
        }
        R.id.reload -> {
            statsList.clear()
            presenter?.getLaunchList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateData(mass: ArrayList<LaunchMassStatsModel>, animate: Boolean) {
        if (statsList.isEmpty()) statsList.addAll(mass)

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

        mass.forEach {
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
                                it.falconOne.PO + it.falconNine.PO + it.falconHeavy.PO,
                                it.falconOne.SSO + it.falconNine.SSO + it.falconHeavy.SSO,
                                it.falconOne.ISS + it.falconNine.ISS + it.falconHeavy.ISS,
                                it.falconOne.HCO + it.falconNine.HCO + it.falconHeavy.HCO,
                                it.falconOne.MEO + it.falconNine.MEO + it.falconHeavy.MEO,
                                it.falconOne.SO + it.falconNine.SO + it.falconHeavy.SO,
                                it.falconOne.ED_L1 + it.falconNine.ED_L1 + it.falconHeavy.ED_L1
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
                    "PO",
                    "SSO",
                    "ISS",
                    "HCO",
                    "MEO",
                    "SO",
                    "ED-L1"
                )
                else -> arrayOf("")
            }
        }

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set)

        binding?.launchMassBarChart?.apply {
            if (animate) animateY(400, Easing.Linear)
            xAxis.labelCount = c
            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = if ((max.toInt() % 2) == 0) max else max.plus(1)
                labelCount =
                    if ((max.toInt() % 2) == 0) max.toInt() / 2 else max.toInt().plus(1) / 2
            }
            data = BarData(dataSets)
            invalidate()
        }
    }

    private fun orbitsToArray(orbitMassModel: OrbitMassModel): FloatArray = floatArrayOf(
        orbitMassModel.LEO,
        orbitMassModel.GTO,
        orbitMassModel.PO,
        orbitMassModel.SSO,
        orbitMassModel.ISS,
        orbitMassModel.HCO,
        orbitMassModel.MEO,
        orbitMassModel.SO,
        orbitMassModel.ED_L1
    )

    override fun showFilter(filterVisible: Boolean) {
        binding?.launchMassFilterConstraint?.visibility = when (filterVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        binding?.launchMassFilterTint?.visibility = when (filterVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        this.filterVisible = filterVisible
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (statsList.isEmpty() || it.progressIndicator.isShown) presenter?.getLaunchList()
            }
        }
    }
}