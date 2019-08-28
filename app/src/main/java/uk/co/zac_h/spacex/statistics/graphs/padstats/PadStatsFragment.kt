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
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.data.LandingPadModel
import uk.co.zac_h.spacex.utils.data.LaunchpadModel
import uk.co.zac_h.spacex.utils.data.StatsPadModel

class PadStatsFragment : Fragment(), PadStatsView {

    private lateinit var presenter: PadStatsPresenter

    private lateinit var padAdapter: PadStatsSitesAdapter

    private var padList = ArrayList<StatsPadModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_pad_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        padAdapter = PadStatsSitesAdapter(padList)

        pad_stats_launch_sites_recycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(true)
            adapter = padAdapter
        }

        presenter.apply {
            getPads()
        }
    }

    override fun setLaunchpadsList(launchpads: List<LaunchpadModel>?) {
        padList.add(StatsPadModel("Launch Sites", 0, 0, "", isHeading = true))
        launchpads?.forEach {
            padList.add(
                StatsPadModel(
                    it.nameLong,
                    it.launchAttempts,
                    it.launchSuccesses,
                    it.status
                )
            )
        }
        padAdapter.notifyDataSetChanged()
    }

    override fun setLandingPadsList(landingPads: List<LandingPadModel>?) {
        padList.add(StatsPadModel("Landing Sites", 0, 0, "", isHeading = true))
        landingPads?.forEach {
            padList.add(
                StatsPadModel(
                    it.nameFull,
                    it.landingAttempts,
                    it.landingSuccesses,
                    it.status,
                    it.type
                )
            )
        }
        padAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        pad_stats_sites_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pad_stats_sites_progress_bar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
