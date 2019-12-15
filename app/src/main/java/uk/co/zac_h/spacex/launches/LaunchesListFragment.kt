package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_launches_list.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchesListFragment : Fragment(), LaunchesView, SearchView.OnQueryTextListener,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private lateinit var presenter: LaunchesPresenter
    private lateinit var launchesAdapter: LaunchesAdapter
    private lateinit var launchesList: ArrayList<LaunchesModel>

    private lateinit var searchView: SearchView

    private var launchParam: String? = null

    companion object {
        fun newInstance(launchParam: String) = LaunchesListFragment().apply {
            arguments = Bundle().apply {
                putString("launchParam", launchParam)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        launchesList = savedInstanceState?.let {
            it.getParcelableArrayList<LaunchesModel>("launches") as ArrayList<LaunchesModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_launches_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchParam = arguments?.getString("launchParam")

        presenter = LaunchesPresenterImpl(this, LaunchesInteractorImpl())

        launchesAdapter = LaunchesAdapter(context, launchesList)

        launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        launchParam?.let { launchId ->
            launches_swipe_refresh.setOnRefreshListener {
                presenter.getLaunchList(launchId)
            }

            if (launchesList.isEmpty()) presenter.getLaunchList(launchId)
        }
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        launches_swipe_refresh.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
        launches_swipe_refresh.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("launches", launchesList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::searchView.isInitialized) searchView.setOnQueryTextListener(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_launches, menu)

        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView

        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        launchesAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        launchesAdapter.filter.filter(newText)
        return false
    }

    override fun updateLaunchesList(launches: List<LaunchesModel>?) {
        launches?.let {
            launchesList.clear()
            launchesList.addAll(it)
        }

        launchesAdapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        launches_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        launches_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        launches_swipe_refresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (launchesList.isEmpty()) launchParam?.let { launchId ->
                presenter.getLaunchList(launchId)
            }
        }
    }
}
