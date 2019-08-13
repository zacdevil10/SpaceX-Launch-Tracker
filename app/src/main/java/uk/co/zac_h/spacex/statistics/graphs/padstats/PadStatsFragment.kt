package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.HORIZONTAL
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pad_stats.*

import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.statistics.adapters.PadStatsLaunchSitesAdapter
import uk.co.zac_h.spacex.utils.data.LaunchpadModel

class PadStatsFragment : Fragment(), PadStatsView {

    private lateinit var presenter: PadStatsPresenter

    private lateinit var launchSitesAdapter: PadStatsLaunchSitesAdapter

    private var launchpads = ArrayList<LaunchpadModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_pad_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        launchSitesAdapter = PadStatsLaunchSitesAdapter(context, launchpads)

        pad_stats_launch_sites_recycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(true)
            adapter = launchSitesAdapter
        }

        if (launchpads.isEmpty()) presenter.getLaunchpads()
    }

    override fun setLaunchesList(launchpads: List<LaunchpadModel>?) {
        if (launchpads != null) {
            this.launchpads.addAll(launchpads)
        }
        launchSitesAdapter.notifyDataSetChanged()
    }

    override fun toggleProgress(visibility: Int) {

    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
