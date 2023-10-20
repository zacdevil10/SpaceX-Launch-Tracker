package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.types.PadType
import uk.co.zac_h.spacex.databinding.FragmentPadStatsBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.dto.spacex.StatsPadModel
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom

class PadStatsFragment : BaseFragment() {

    private lateinit var binding: FragmentPadStatsBinding

    private val viewModel: PadStatsViewModel by viewModels()

    private val navArgs: PadStatsFragmentArgs by navArgs()

    private lateinit var padsAdapter: PadStatsSitesAdapter

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
    ): View = FragmentPadStatsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.padStatsLaunchSitesRecycler.transitionName = getString(navArgs.type.title)

        padsAdapter = PadStatsSitesAdapter()

        binding.padStatsLaunchSitesRecycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(true)
            adapter = padsAdapter
        }

        when (navArgs.padType) {
            PadType.LANDING_PAD -> viewModel.getLandingPads()
            PadType.LAUNCHPAD -> viewModel.getLaunchpads()
        }

        viewModel.stats.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Pending -> {}
                is ApiResult.Success -> update(response.result)
                is ApiResult.Failure -> showError(response.exception.message)
            }
        }
    }

    private fun update(response: List<StatsPadModel>) {
        binding.padStatsLaunchSitesRecycler.layoutAnimation =
            animateLayoutFromBottom(requireContext())
        padsAdapter.submitList(response)
        binding.padStatsLaunchSitesRecycler.scheduleLayoutAnimation()
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when (navArgs.padType) {
            PadType.LANDING_PAD -> viewModel.getLandingPads()
            PadType.LAUNCHPAD -> viewModel.getLaunchpads()
        }
    }
}
