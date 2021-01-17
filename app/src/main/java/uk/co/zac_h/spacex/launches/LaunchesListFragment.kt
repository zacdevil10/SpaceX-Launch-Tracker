package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchesListBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class LaunchesListFragment : Fragment(), NetworkInterface.View<List<Launch>>,
    SearchView.OnQueryTextListener, OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var binding: FragmentLaunchesListBinding? = null

    private var presenter: NetworkInterface.Presenter<Nothing>? = null
    private lateinit var launchesAdapter: LaunchesAdapter
    private lateinit var launches: ArrayList<Launch>

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

        launches = savedInstanceState?.let {
            it.getParcelableArrayList<Launch>("launches") as ArrayList<Launch>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesListBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        launchParam = arguments?.getString("launchParam")

        presenter = LaunchesPresenterImpl(this, LaunchesInteractorImpl())

        launchesAdapter = LaunchesAdapter(context, launches)

        binding?.launchesRecycler?.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        launchParam?.let { launchId ->
            binding?.launchesSwipeRefresh?.setOnRefreshListener {
                presenter?.get(launchId)
            }

            if (launches.isEmpty()) presenter?.get(launchId)
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
        outState.putParcelableArrayList("launches", launches)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
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

    override fun update(response: List<Launch>) {
        launches.clear()
        launches.addAll(response)


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

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.launchesSwipeRefresh?.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (launches.isEmpty() || it.progressIndicator.isShown)
                    launchParam?.let { launchId ->
                        presenter?.get(launchId)
                    }
            }
        }
    }
}
