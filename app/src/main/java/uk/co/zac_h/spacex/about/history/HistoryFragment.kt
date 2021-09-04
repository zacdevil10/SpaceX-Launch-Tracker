package uk.co.zac_h.spacex.about.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.openWebLink
import uk.co.zac_h.spacex.utils.orUnknown
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

@AndroidEntryPoint
class HistoryFragment : BaseFragment() {

    override val title: String by lazy { getString(R.string.menu_history) }

    private val viewModel: HistoryViewModel by viewModels()

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

        binding.toolbarLayout.toolbar.apply {
            setup()
            createOptionsMenu(R.menu.menu_history)
        }

        historyAdapter = HistoryAdapter(requireContext(), ::openWebLink)

        val isTabletLand = resources.getBoolean(R.bool.isTabletLand)

        binding.historyRecycler.apply {
            layoutManager = if (isTabletLand) LinearLayoutManager(
                this@HistoryFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            ) else LinearLayoutManager(this@HistoryFragment.context)
            adapter = historyAdapter
            addItemDecoration(HeaderItemDecoration(this, historyAdapter.isHeader(), isTabletLand))
            itemAnimator = null
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getHistory(CachePolicy.REFRESH)
        }

        viewModel.history.observe(viewLifecycleOwner) {
            when (it.status) {
                ApiResult.Status.PENDING -> showProgress()
                ApiResult.Status.SUCCESS -> {
                    hideProgress()
                    toggleSwipeRefresh(false)
                    it.data?.let { data -> update(data) }
                }
                ApiResult.Status.FAILURE -> {
                    showError(it.error?.message.orUnknown())
                    toggleSwipeRefresh(false)
                }
            }
        }

        viewModel.getHistory()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sort_new -> handleSortItemClick(false)
        R.id.sort_old -> handleSortItemClick(true)
        else -> super.onOptionsItemSelected(item)
    }

    private fun handleSortItemClick(order: Boolean): Boolean {
        if (viewModel.getOrder() == order) viewModel.apply {
            setOrder(!order)
            getHistory(CachePolicy.REFRESH)
        }
        return true
    }

    fun update(response: List<HistoryHeaderModel>) {
        historyAdapter.submitList(response)

        binding.historyRecycler.apply {
            smoothScrollToPosition(0)
            scheduleLayoutAnimation()
        }
    }

    fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        viewModel.getHistory()
    }
}
