package uk.co.zac_h.spacex.vehicles.cores

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.dto.spacex.Core
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.vehicles.adapters.CoreAdapter

class CoreFragment : BaseFragment(), SearchView.OnQueryTextListener {

    override var title: String = "Cores"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: CoreViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var coreAdapter: CoreAdapter

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coreAdapter = CoreAdapter(requireContext()) { viewModel.selectedId = it }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = coreAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getCores(CachePolicy.REFRESH)
        }

        viewModel.cores.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    result.data?.let { data -> update(data) }
                }
                ApiResult.Status.FAILURE -> {
                    binding.swipeRefresh.isRefreshing = false
                    showError(result.error?.message)
                }
            }
        }

        viewModel.getCores()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_vehicles_cores, menu)

        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sort_new -> handleSortItemClick(false)
        R.id.sort_old -> handleSortItemClick(true)
        else -> super.onOptionsItemSelected(item)
    }

    private fun handleSortItemClick(order: Boolean): Boolean {
        if (viewModel.getOrder() == order) viewModel.apply {
            setOrder(!order)
            getCores()
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //coreAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //coreAdapter.filter.filter(newText)
        return false
    }

    private fun update(response: List<Core>) {
        hideProgress()
        coreAdapter.submitList(response)
        if (viewModel.cacheLocation == Repository.RequestLocation.REMOTE) {
            binding.recycler.layoutAnimation = animateLayoutFromBottom(requireContext())
            binding.recycler.scheduleLayoutAnimation()
        }
    }

    private fun showProgress() {
        binding.progress.show()
    }

    private fun hideProgress() {
        binding.progress.hide()
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getCores()
    }
}
