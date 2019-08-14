package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pad_stats.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.statistics.adapters.PadStatsLandingSitesAdapter
import uk.co.zac_h.spacex.statistics.adapters.PadStatsLaunchSitesAdapter
import uk.co.zac_h.spacex.utils.data.LandingPadModel
import uk.co.zac_h.spacex.utils.data.LaunchpadModel

class PadStatsFragment : Fragment(), PadStatsView {

    private lateinit var presenter: PadStatsPresenter

    private lateinit var launchSitesAdapter: PadStatsLaunchSitesAdapter
    private lateinit var landingSitesAdapter: PadStatsLandingSitesAdapter

    private var launchpads = ArrayList<LaunchpadModel>()
    private var landingPads = ArrayList<LandingPadModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_pad_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        launchSitesAdapter = PadStatsLaunchSitesAdapter(launchpads)
        landingSitesAdapter = PadStatsLandingSitesAdapter(landingPads)

        pad_stats_launch_sites_recycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(false)
            adapter = launchSitesAdapter
        }

        pad_stats_landing_sites_recycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(false)
            adapter = landingSitesAdapter
        }

        if (launchpads.isEmpty()) presenter.getLaunchpads()
        if (landingPads.isEmpty()) presenter.getLandingPads()
    }

    override fun setLaunchpadsList(launchpads: List<LaunchpadModel>?) {
        if (launchpads != null) {
            this.launchpads.addAll(launchpads)
        }
        launchSitesAdapter.notifyDataSetChanged()
    }

    override fun setLandingPadsList(landingPads: List<LandingPadModel>?) {
        if (landingPads != null) {
            this.landingPads.addAll(landingPads)
        }
        landingSitesAdapter.notifyDataSetChanged()
    }

    override fun toggleLaunchpadsProgress(visibility: Int) {
        pad_stats_launch_sites_progress_bar.visibility = visibility
    }

    override fun toggleLandingPadsProgress(visibility: Int) {
        pad_stats_landing_sites_progress_bar.visibility = visibility
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
