package uk.co.zac_h.spacex.feature.assets.astronauts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.recyclerview.PagingLoadStateAdapter
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentVerticalRecyclerviewBinding

class AstronautFragment : BaseFragment(), ViewPagerFragment {

    override val title: String = "Astronauts"

    private val viewModel: AstronautViewModel by viewModels()

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
            adapter = astronautsAdapter.withLoadStateFooter(
                footer = PagingLoadStateAdapter(astronautsAdapter::retry)
            )
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
                    binding.progress.hide()

                    val error = when {
                        it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                        it.append is LoadState.Error -> it.append as LoadState.Error
                        it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                        else -> null
                    }

                    error?.error?.let { message -> showError(message) }
                }
            }
        }
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        Log.e("AstronautFragment", error.message.orUnknown())
    }

    override fun networkAvailable() {
        astronautsAdapter.retry()
    }
}