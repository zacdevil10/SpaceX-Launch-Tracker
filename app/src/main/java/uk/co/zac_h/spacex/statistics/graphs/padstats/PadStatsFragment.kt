package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pad_stats.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.model.spacex.StatsPadModel
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.HeaderItemDecoration
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class PadStatsFragment : Fragment(), PadStatsContract.PadStatsView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: PadStatsContract.PadStatsPresenter? = null

    private lateinit var padsAdapter: PadStatsSitesAdapter

    private lateinit var pads: ArrayList<StatsPadModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        pads = savedInstanceState?.getParcelableArrayList<StatsPadModel>("pads") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pad_stats, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        padsAdapter = PadStatsSitesAdapter(pads)

        pad_stats_launch_sites_recycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(true)
            adapter = padsAdapter
            addItemDecoration(HeaderItemDecoration(this, padsAdapter.isHeader(), false))
        }

        if (pads.isEmpty()) presenter?.getPads()
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
        outState.putParcelableArrayList("pads", pads)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_statistics_pads, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.reload -> {
            presenter?.getPads()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun updateRecycler(pads: ArrayList<StatsPadModel>) {
        this.pads.clear()
        this.pads.addAll(pads)

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

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (pads.isEmpty() || pad_stats_sites_progress_bar.visibility == View.VISIBLE) presenter?.getPads()
        }
    }
}
