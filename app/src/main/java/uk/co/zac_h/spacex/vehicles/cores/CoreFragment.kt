package uk.co.zac_h.spacex.vehicles.cores

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_core.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.vehicles.adapters.CoreAdapter

class CoreFragment : Fragment(), CoreView, SearchView.OnQueryTextListener,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: CorePresenter? = null

    private lateinit var coreAdapter: CoreAdapter
    private lateinit var coresArray: ArrayList<CoreModel>

    private var sortNew = false
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        coresArray = savedInstanceState?.getParcelableArrayList<CoreModel>("cores") ?: ArrayList()
        sortNew = savedInstanceState?.getBoolean("sort") ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_core, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CorePresenterImpl(this, CoreInteractorImpl())

        coreAdapter = CoreAdapter(context, coresArray)

        core_recycler.apply {
            layoutManager = LinearLayoutManager(this@CoreFragment.context)
            setHasFixedSize(true)
            adapter = coreAdapter
        }

        core_swipe_refresh.setOnRefreshListener {
            presenter?.getCores()
        }

        if (coresArray.isEmpty()) presenter?.getCores()
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

    override fun updateCores(cores: List<CoreModel>) {
        coresArray.clear()
        coresArray.addAll(cores)

        coreAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        core_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        core_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeRefresh(refreshing: Boolean) {
        core_swipe_refresh.isRefreshing = refreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (coresArray.isEmpty() || core_progress_bar.visibility == View.VISIBLE) presenter?.getCores()
        }
    }
}
