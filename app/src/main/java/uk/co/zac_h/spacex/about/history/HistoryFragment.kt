package uk.co.zac_h.spacex.about.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.CachePolicy
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.about.history.filter.HistoryFilterViewModel
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

    private val filterViewModel: HistoryFilterViewModel by activityViewModels()

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var historyAdapter: HistoryAdapter

    private var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialElevationScale(true)
        exitTransition = MaterialElevationScale(false)
    }

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
                    update(it.data)
                    binding.swipeRefresh.isRefreshing = false
                }
                ApiResult.Status.FAILURE -> {
                    showError(it.error?.message.orUnknown())
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        filterViewModel.searchText.observe(viewLifecycleOwner) {
            searchText = it
            viewModel.getHistory()
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

    fun update(response: List<HistoryHeaderModel>?) {
        historyAdapter.submitList(response?.filter {
            it.historyModel?.title?.lowercase()?.contains(searchText) ?: true
        }) {
            binding.historyRecycler.apply {
                smoothScrollToPosition(0)
            }
        }
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
