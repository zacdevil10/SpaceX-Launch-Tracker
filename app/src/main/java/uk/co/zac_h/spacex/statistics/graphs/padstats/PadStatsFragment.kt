package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentPadStatsBinding
import uk.co.zac_h.spacex.dto.spacex.StatsPadModel
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.PadType
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom

class PadStatsFragment : BaseFragment() {

    override val title: String by lazy { getString(navArgs.type.title) }

    private lateinit var binding: FragmentPadStatsBinding

    private val viewModel: PadStatsViewModel by viewModels()

    private val navArgs: PadStatsFragmentArgs by navArgs()

    private lateinit var padsAdapter: PadStatsSitesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()
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

        binding.toolbarLayout.toolbar.apply {
            setup()
            createOptionsMenu(R.menu.menu_statistics_reload)
        }

        binding.padStatsConstraint.transitionName = getString(navArgs.type.title)

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
            when (response.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> response.data?.let {
                    update(it)
                }
                ApiResult.Status.FAILURE -> showError(response.error?.message)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            when (navArgs.padType) {
                PadType.LANDING_PAD -> viewModel.getLandingPads(CachePolicy.REFRESH)
                PadType.LAUNCHPAD -> viewModel.getLaunchpads(CachePolicy.REFRESH)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun update(response: List<StatsPadModel>) {
        hideProgress()
        binding.padStatsLaunchSitesRecycler.layoutAnimation =
            animateLayoutFromBottom(requireContext())
        padsAdapter.submitList(response)
        binding.padStatsLaunchSitesRecycler.scheduleLayoutAnimation()
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
        when (navArgs.padType) {
            PadType.LANDING_PAD -> viewModel.getLandingPads()
            PadType.LAUNCHPAD -> viewModel.getLaunchpads()
        }
    }
}
