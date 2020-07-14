package uk.co.zac_h.spacex.vehicles.cores

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentCoreBinding
import uk.co.zac_h.spacex.model.spacex.CoreExtendedModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.VehiclesContract
import uk.co.zac_h.spacex.vehicles.adapters.CoreAdapter

class CoreFragment : Fragment(), VehiclesContract.View<CoreExtendedModel>,
    SearchView.OnQueryTextListener,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentCoreBinding? = null
    private val binding get() = _binding!!

    private var presenter: VehiclesContract.Presenter? = null

    private lateinit var coreAdapter: CoreAdapter
    private lateinit var coresArray: ArrayList<CoreExtendedModel>

    private var sortNew = false
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        coresArray = savedInstanceState?.getParcelableArrayList("cores") ?: ArrayList()
        sortNew = savedInstanceState?.getBoolean("sort") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        presenter = CorePresenterImpl(this, CoreInteractorImpl())

        coreAdapter = CoreAdapter(context, coresArray)

        binding.coreRecycler.apply {
            layoutManager = LinearLayoutManager(this@CoreFragment.context)
            setHasFixedSize(true)
            adapter = coreAdapter
        }

        binding.coreSwipeRefresh.setOnRefreshListener {
            presenter?.getVehicles()
        }

        if (coresArray.isEmpty()) presenter?.getVehicles()
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this@CoreFragment)
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("cores", coresArray)
        outState.putBoolean("sort", sortNew)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_vehicles_cores, menu)

        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.sort_new -> {
                if (!sortNew) {
                    sortNew = true
                    coresArray.reverse()
                    coreAdapter.notifyDataSetChanged()
                }
                true
            }
            R.id.sort_old -> {
                if (sortNew) {
                    sortNew = false
                    coresArray.reverse()
                    coreAdapter.notifyDataSetChanged()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onQueryTextSubmit(query: String?): Boolean {
        coreAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        coreAdapter.filter.filter(newText)
        return false
    }

    override fun updateVehicles(vehicles: List<CoreExtendedModel>) {
        coresArray.clear()
        coresArray.addAll(if (sortNew) vehicles.reversed() else vehicles)

        binding.coreRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        coreAdapter.notifyDataSetChanged()
        binding.coreRecycler.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding.progressIndicator.show()
    }

    override fun hideProgress() {
        binding.progressIndicator.hide()
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        binding.coreSwipeRefresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (coresArray.isEmpty() || binding.progressIndicator.isShown)
                presenter?.getVehicles()
        }
    }
}
