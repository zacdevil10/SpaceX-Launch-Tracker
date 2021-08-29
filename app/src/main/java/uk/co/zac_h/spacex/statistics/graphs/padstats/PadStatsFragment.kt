package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import android.view.*
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentPadStatsBinding
import uk.co.zac_h.spacex.dto.spacex.StatsPadModel
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.PadType
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.utils.clearAndAdd

class PadStatsFragment : BaseFragment(), NetworkInterface.View<List<StatsPadModel>> {

    override val title: String by lazy { heading ?: "Pads" }

    private lateinit var binding: FragmentPadStatsBinding

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
        //pads = savedInstanceState?.getParcelableArrayList("pads") ?: ArrayList()
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

        binding.padStatsConstraint.transitionName = heading

        presenter = PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        padsAdapter = PadStatsSitesAdapter(pads)

        binding.padStatsLaunchSitesRecycler.apply {
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
        //outState.putParcelableArrayList("pads", pads)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {

            when (type) {
                PadType.LANDING_PAD -> presenter?.getLandingPads()
                PadType.LAUNCHPAD -> presenter?.getLaunchpads()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun update(response: List<StatsPadModel>) {

        pads.clearAndAdd(response)

        binding.padStatsLaunchSitesRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
        padsAdapter.notifyDataSetChanged()
        binding.padStatsLaunchSitesRecycler.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    override fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        /*when (apiState) {
            ApiResult.Status.PENDING, ApiResult.Status.FAILURE -> when (type) {
                PadType.LANDING_PAD -> presenter?.getLandingPads()
                PadType.LAUNCHPAD -> presenter?.getLaunchpads()
            }
            ApiResult.Status.SUCCESS -> {
            }
        }*/
    }
}
