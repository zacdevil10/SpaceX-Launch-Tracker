package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentPadStatsBinding
import uk.co.zac_h.spacex.model.spacex.StatsPadModel
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.PadType
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.utils.clearAndAdd

class PadStatsFragment : BaseFragment(), NetworkInterface.View<List<StatsPadModel>> {

    override val title: String by lazy { heading ?: "Pads" }

    private var _binding: FragmentPadStatsBinding? = null
    private val binding get() = _binding!!

    private var heading: String? = null

    private var presenter: PadStatsContract.PadStatsPresenter? = null

    private var type: PadType? = null

    private lateinit var padsAdapter: PadStatsSitesAdapter

    private lateinit var pads: ArrayList<StatsPadModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedElementEnterTransition = MaterialContainerTransform()

        heading = arguments?.getString("heading")
        type = arguments?.get("type") as PadType?
        pads = savedInstanceState?.getParcelableArrayList("pads") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPadStatsBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
        binding.toolbarLayout.toolbar.setup()

        binding?.padStatsConstraint?.transitionName = heading

        hideProgress()

        presenter = PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        padsAdapter = PadStatsSitesAdapter(pads)

        binding?.padStatsLaunchSitesRecycler?.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(true)
            adapter = padsAdapter
        }

        type?.let {
            when (it) {
                PadType.LANDING_PAD -> presenter?.getLandingPads()
                PadType.LAUNCHPAD -> presenter?.getLaunchpads()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("pads", pads)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_reload, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            apiState = ApiState.PENDING
            when (type) {
                PadType.LANDING_PAD -> presenter?.getLandingPads()
                PadType.LAUNCHPAD -> presenter?.getLaunchpads()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(response: List<StatsPadModel>) {
        apiState = ApiState.SUCCESS
        pads.clearAndAdd(response)

        binding?.padStatsLaunchSitesRecycler?.layoutAnimation = animateLayoutFromBottom(context)
        padsAdapter.notifyDataSetChanged()
        binding?.padStatsLaunchSitesRecycler?.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.toolbarLayout.progress?.show()
    }

    override fun hideProgress() {
        binding.toolbarLayout.progress?.hide()
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> when (type) {
                PadType.LANDING_PAD -> presenter?.getLandingPads()
                PadType.LAUNCHPAD -> presenter?.getLaunchpads()
            }
            ApiState.SUCCESS -> {
            }
        }
    }
}
