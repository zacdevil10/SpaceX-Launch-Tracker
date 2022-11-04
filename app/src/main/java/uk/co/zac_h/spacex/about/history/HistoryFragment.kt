package uk.co.zac_h.spacex.about.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.about.history.filter.HistoryFilterViewModel
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.fragment.openWebLink
import uk.co.zac_h.spacex.core.common.utils.orUnknown
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

@AndroidEntryPoint
class HistoryFragment : BaseFragment() {

    private val viewModel: HistoryViewModel by viewModels()

    private val filterViewModel: HistoryFilterViewModel by activityViewModels()

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHistoryBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnPreDraw { startPostponedEnterTransition() }
        postponeEnterTransition()

        historyAdapter = HistoryAdapter {
            openWebLink(it)
        }

        val isTabletLandscape = resources.getBoolean(R.bool.isTabletLand)

        binding.historyRecycler.apply {
            layoutManager = if (isTabletLandscape) LinearLayoutManager(
                this@HistoryFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            ) else LinearLayoutManager(this@HistoryFragment.context)
            adapter = historyAdapter
            addItemDecoration(
                HeaderItemDecoration(this, historyAdapter.isHeader(), isTabletLandscape)
            )
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getHistory(CachePolicy.REFRESH)
        }

        viewModel.history.observe(viewLifecycleOwner) {
            when (it) {
                is ApiResult.Pending -> if (historyAdapter.itemCount == 0) showProgress()
                is ApiResult.Success -> {
                    if (historyAdapter.itemCount == 0) hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                    historyAdapter.submitList(it.data) {
                        binding.historyRecycler.smoothScrollToPosition(0)
                    }
                }
                is ApiResult.Failure -> {
                    showError(it.exception.message.orUnknown())
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        filterViewModel.order.observe(viewLifecycleOwner) {
            viewModel.order = it
            viewModel.getHistory(CachePolicy.REFRESH)
        }

        viewModel.getHistory()
    }

    private fun showProgress() {
        binding.historyRecycler.visibility = View.INVISIBLE
        binding.progress.show()
    }

    private fun hideProgress() {
        binding.historyRecycler.visibility = View.VISIBLE
        binding.progress.hide()
    }

    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        viewModel.getHistory()
    }
}
