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
import uk.co.zac_h.spacex.model.StatsPadModel
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.HeaderItemDecoration

class PadStatsFragment : Fragment(), PadStatsView {

    private lateinit var presenter: PadStatsPresenter

    private lateinit var padsAdapter: PadStatsSitesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_pad_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!::presenter.isInitialized) presenter =
            PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        presenter.getPads()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pad_stats_launch_sites_recycler.adapter = null
    }

    override fun setPadsList(pads: ArrayList<StatsPadModel>) {
        padsAdapter = PadStatsSitesAdapter(pads)

        pad_stats_launch_sites_recycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(true)
            adapter = padsAdapter
            addItemDecoration(HeaderItemDecoration(this, padsAdapter.isHeader()))
        }
    }

    override fun updateRecycler() {
        padsAdapter.notifyDataSetChanged()
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
