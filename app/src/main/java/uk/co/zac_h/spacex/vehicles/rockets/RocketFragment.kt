package uk.co.zac_h.spacex.vehicles.rockets

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
import uk.co.zac_h.spacex.databinding.FragmentRocketBinding
import uk.co.zac_h.spacex.dto.spacex.Rocket
import uk.co.zac_h.spacex.utils.animateLayoutFromBottom
import uk.co.zac_h.spacex.utils.orUnknown
import uk.co.zac_h.spacex.vehicles.adapters.RocketsAdapter

class RocketFragment : BaseFragment() {

    override var title: String = "Rockets"

    private lateinit var binding: FragmentRocketBinding

    private val viewModel: RocketViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var rocketsAdapter: RocketsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRocketBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rocketsAdapter = RocketsAdapter(::selected)

        binding.rocketRecycler.apply {
            layoutManager = LinearLayoutManager(this@RocketFragment.context)
            setHasFixedSize(true)
            adapter = rocketsAdapter
        }

        binding.rocketSwipeRefresh.setOnRefreshListener {
            viewModel.getRockets(CachePolicy.REFRESH)
        }

        viewModel.rockets.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                ApiResult.Status.PENDING -> {
                }
                ApiResult.Status.SUCCESS -> {
                    binding.rocketSwipeRefresh.isRefreshing = false
                    result.data?.let { data -> update(data) }
                }
                ApiResult.Status.FAILURE -> {
                    binding.rocketSwipeRefresh.isRefreshing = false
                    showError(result.error?.message.orUnknown())
                }
            }
        }

        viewModel.getRockets()
    }

    private fun update(response: List<Rocket>) {
        rocketsAdapter.submitList(response)
        if (viewModel.cacheLocation == Repository.RequestLocation.REMOTE) {
            binding.rocketRecycler.layoutAnimation = animateLayoutFromBottom(requireContext())
            binding.rocketRecycler.scheduleLayoutAnimation()
        }
    }

    private fun selected(id: String) {
        viewModel.setSelected(id)
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getRockets()
    }
}
