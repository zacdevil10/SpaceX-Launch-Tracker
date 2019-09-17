package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_launches_list.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.model.LaunchesModel

class LaunchesListFragment : Fragment(), LaunchesView, SearchView.OnQueryTextListener {

    private lateinit var presenter: LaunchesPresenter
    private lateinit var launchesAdapter: LaunchesAdapter
    private var launchesList = ArrayList<LaunchesModel>()

    private lateinit var searchView: SearchView

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_launches_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = LaunchesPresenterImpl(this, LaunchesInteractorImpl())

        launchesAdapter = LaunchesAdapter(context, launchesList)

        launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        arguments?.getString("launchParam")?.let { launchId ->
            if (launchesList.isEmpty()) presenter.getLaunchList(
                launchId,
                if (launchId == "past") "desc" else "asc"
            )

            launches_swipe_refresh.setOnRefreshListener {
                presenter.getLaunchList(launchId, if (launchId == "past") "desc" else "asc")
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancelRequests()
        if (::searchView.isInitialized) searchView.setOnQueryTextListener(null)
    }
}
