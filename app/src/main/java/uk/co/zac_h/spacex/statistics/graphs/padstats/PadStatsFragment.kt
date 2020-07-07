package uk.co.zac_h.spacex.statistics.graphs.padstats

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentPadStatsBinding
import uk.co.zac_h.spacex.model.spacex.StatsPadModel
import uk.co.zac_h.spacex.statistics.adapters.PadStatsSitesAdapter
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

class PadStatsFragment : Fragment(), PadStatsContract.PadStatsView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentPadStatsBinding? = null
    private val binding get() = _binding!!

    private var presenter: PadStatsContract.PadStatsPresenter? = null

    private lateinit var padsAdapter: PadStatsSitesAdapter

    private lateinit var pads: ArrayList<StatsPadModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        pads = savedInstanceState?.getParcelableArrayList("pads") ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPadStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = PadStatsPresenterImpl(this, PadStatsInteractorImpl())

        padsAdapter = PadStatsSitesAdapter(pads)

        binding.padStatsLaunchSitesRecycler.apply {
            layoutManager = LinearLayoutManager(this@PadStatsFragment.context)
            setHasFixedSize(true)
            adapter = padsAdapter
            addItemDecoration(
                HeaderItemDecoration(
                    this,
                    padsAdapter.isHeader(),
                    false
                )
            )
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
        _binding = null
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
        binding.progressIndicator.show()
    }

    override fun hideProgress() {
        binding.progressIndicator.hide()
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (pads.isEmpty() || binding.progressIndicator.isShown) presenter?.getPads()
        }
    }
}
