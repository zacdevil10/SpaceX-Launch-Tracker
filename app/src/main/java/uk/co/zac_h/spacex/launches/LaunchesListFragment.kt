package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentLaunchesListBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchesListFragment : Fragment(), LaunchesContract.LaunchesView,
    SearchView.OnQueryTextListener, OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchesListBinding? = null

    private var presenter: LaunchesContract.LaunchesPresenter? = null
    private lateinit var launchesAdapter: LaunchesAdapter
    private lateinit var launchesList: ArrayList<LaunchesExtendedModel>

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
            it.getParcelableArrayList<LaunchesExtendedModel>("launches") as ArrayList<LaunchesExtendedModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchesListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        launchParam = arguments?.getString("launchParam")

        presenter = LaunchesPresenterImpl(this, LaunchesInteractorImpl())

        launchesAdapter = LaunchesAdapter(context, launchesList)

        binding?.launchesRecycler?.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        launchParam?.let { launchId ->
            binding?.launchesSwipeRefresh?.setOnRefreshListener {
                presenter?.getLaunchList(launchId)
            }

            if (launchesList.isEmpty()) presenter?.getLaunchList(launchId)
        }
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        binding?.launchesSwipeRefresh?.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        binding?.launchesSwipeRefresh?.isEnabled = false
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("launches", launchesList)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequests()
        binding = null
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

    override fun updateLaunchesList(launches: List<LaunchesExtendedModel>?) {
        launches?.let {
            launchesList.clear()
            launchesList.addAll(it)
        }

        binding?.launchesRecycler?.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        launchesAdapter.notifyDataSetChanged()
        binding?.launchesRecycler?.scheduleLayoutAnimation()
    }

    override fun showProgress() {
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        binding?.launchesSwipeRefresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (launchesList.isEmpty() || it.progressIndicator.isShown)
                    launchParam?.let { launchId ->
                        presenter?.getLaunchList(launchId)
                    }
            }
        }
    }
}
