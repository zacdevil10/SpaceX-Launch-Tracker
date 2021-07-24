package uk.co.zac_h.spacex.launches

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentLaunchesListBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import java.util.*
import kotlin.collections.ArrayList

class LaunchesListFragment : BaseFragment(), NetworkInterface.View<List<Launch>>,
    SearchView.OnQueryTextListener {

    override var title: String = ""

    private lateinit var binding: FragmentLaunchesListBinding
    private var presenter: NetworkInterface.Presenter<List<Launch>>? = null
    private lateinit var launchesAdapter: LaunchesAdapter

    private lateinit var launches: ArrayList<Launch>

    private var searchView: SearchView? = null

    private var launchParam: String? = null

    companion object {
        fun newInstance(launchParam: String) = LaunchesListFragment().apply {
            arguments = bundleOf("launchParam" to launchParam)
            title = launchParam
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        launches = savedInstanceState?.getParcelableArrayList("launches") ?: ArrayList()
        launchParam = arguments?.getString("launchParam")
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

        presenter = LaunchesPresenterImpl(this, LaunchesInteractorImpl())

        launchesAdapter = LaunchesAdapter(requireContext())

        binding.launchesRecycler.apply {
            layoutManager = LinearLayoutManager(this@LaunchesListFragment.context)
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        launchParam?.let { launchId ->
            binding.swipeRefresh.setOnRefreshListener {
                apiState = ApiState.PENDING
                presenter?.get(launchId)
            }

            presenter?.getOrUpdate(launches, launchId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("launches", launches)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        searchView?.setOnQueryTextListener(null)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        /*inflater.inflate(R.menu.menu_launches, menu)

        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView

        searchView?.setOnQueryTextListener(this)*/

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //launchesAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //launchesAdapter.filter.filter(newText)
        return false
    }

    override fun update(response: List<Launch>) {
        apiState = ApiState.SUCCESS

        launches = response as ArrayList<Launch>

        launchesAdapter.update(response)
    }

    override fun showProgress() {
        binding.progress.show()
    }

    override fun hideProgress() {
        binding.progress.hide()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> launchParam?.let {
                presenter?.getOrUpdate(launches, it)
            }
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }
}
