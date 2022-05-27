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
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.about.history.filter.HistoryFilterViewModel
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.utils.openWebLink
import uk.co.zac_h.spacex.utils.orUnknown
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

@AndroidEntryPoint
class HistoryFragment : BaseFragment() {

    override val title: String by lazy { getString(R.string.menu_history) }

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

        historyAdapter = HistoryAdapter(::openWebLink)

        val isTabletLand = resources.getBoolean(R.bool.isTabletLand)

        binding.historyRecycler.apply {
            layoutManager = if (isTabletLand) LinearLayoutManager(
                this@HistoryFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            ) else LinearLayoutManager(this@HistoryFragment.context)
            adapter = historyAdapter
            addItemDecoration(HeaderItemDecoration(this, historyAdapter.isHeader(), isTabletLand))
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getHistory(CachePolicy.REFRESH)
        }

        viewModel.history.observe(viewLifecycleOwner) {
            when (it.status) {
                ApiResult.Status.PENDING -> if (historyAdapter.itemCount == 0) showProgress()
                ApiResult.Status.SUCCESS -> {
                    if (historyAdapter.itemCount == 0) hideProgress()
                    binding.swipeRefresh.isRefreshing = false
                    historyAdapter.submitList(it.data) {
                        binding.historyRecycler.smoothScrollToPosition(0)
                    }
                }
                ApiResult.Status.FAILURE -> {
                    showError(it.error?.message.orUnknown())
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
