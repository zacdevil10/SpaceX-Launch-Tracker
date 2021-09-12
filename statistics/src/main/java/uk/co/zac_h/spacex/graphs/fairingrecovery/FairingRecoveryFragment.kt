package uk.co.zac_h.spacex.graphs.fairingrecovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.adapters.StatisticsKeyAdapter
import uk.co.zac_h.spacex.models.FairingRecoveryModel
import uk.co.zac_h.spacex.models.KeysModel
import uk.co.zac_h.spacex.statistics.R
import uk.co.zac_h.spacex.statistics.databinding.FragmentFairingRecoveryBinding

@AndroidEntryPoint
class FairingRecoveryFragment : Fragment() {

    //override val title by lazy { getString(Statistics.FAIRING_RECOVERY.title) }

    private lateinit var binding: FragmentFairingRecoveryBinding

    private val viewModel: FairingRecoveryViewModel by viewModels()

    private val navArgs: FairingRecoveryFragmentArgs by navArgs()

    private var statsList: List<FairingRecoveryModel> = emptyList()

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

        viewModel.get()

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_reload)
            setOnMenuItemClickListener {
                onOptionsItemSelected(it)
            }
        }

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
        R.id.reload -> true.also { viewModel.get(CachePolicy.REFRESH) }
        else -> super.onOptionsItemSelected(item)
    }

    fun update(data: Boolean, response: List<FairingRecoveryModel>) {
        hideProgress()
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

    private fun showProgress() {
        binding.progress.show()
    }

    private fun hideProgress() {
        binding.progress.hide()
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    /*override fun networkAvailable() {
        viewModel.get()
    }*/
}