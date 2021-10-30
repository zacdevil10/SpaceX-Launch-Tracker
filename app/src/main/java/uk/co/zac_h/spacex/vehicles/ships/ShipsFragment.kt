package uk.co.zac_h.spacex.vehicles.ships

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentVerticalRecyclerviewBinding
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.vehicles.adapters.ShipsAdapter

class ShipsFragment : BaseFragment() {

    override var title: String = "Ships"

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private val viewModel: ShipsViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var shipsAdapter: ShipsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shipsAdapter = ShipsAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@ShipsFragment.context)
            setHasFixedSize(true)
            adapter = shipsAdapter
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getShips(CachePolicy.REFRESH)
        }

        viewModel.ships.observe(viewLifecycleOwner) { result ->
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

        viewModel.getShips()
    }

    private fun update(response: List<Ship>) {
        hideProgress()
        shipsAdapter.submitList(response)
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
        viewModel.getShips()
    }
}