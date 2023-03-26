package uk.co.zac_h.spacex.feature.astronauts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding

class AstronautFragment : BaseFragment() {

    private val viewModel: AstronautViewModel by navGraphViewModels(R.id.astronauts_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentVerticalRecyclerviewBinding

    private lateinit var astronautsAdapter: AstronautsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVerticalRecyclerviewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        astronautsAdapter = AstronautsAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = astronautsAdapter
        }

        viewModel.astronautLiveData.observe(viewLifecycleOwner) { pagingData ->
            astronautsAdapter.submitData(lifecycle, pagingData.map { AstronautItem(it) })
        }

        binding.swipeRefresh.setOnRefreshListener {
            astronautsAdapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            astronautsAdapter.loadStateFlow.collectLatest {
                if (it.refresh is LoadState.Loading) {
                    binding.progress.show()
                } else {
                    binding.swipeRefresh.isRefreshing = false

                    if (it.refresh is LoadState.NotLoading) binding.progress.hide()

                    val error = when {
                        it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                        it.append is LoadState.Error -> it.append as LoadState.Error
                        it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                        else -> null
                    }

                    error?.error?.message?.let { message -> showError(message) }
                }
            }
        }
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        astronautsAdapter.retry()
    }
}
