package uk.co.zac_h.spacex.about.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

class HistoryFragment : BaseFragment(), HistoryView {

    companion object {
        const val HISTORY_KEY = "history"
    }

    override val title: String by lazy { requireContext().getString(R.string.menu_history) }

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private var presenter: NetworkInterface.Presenter<Nothing>? = null

    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var orderSharedPreferences: OrderSharedPreferencesHelper
    private var sortNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHistoryBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.toolbar.apply {
            setSupportActionBar()
            setup()
        }

        orderSharedPreferences = OrderSharedPreferencesHelperImpl.build(context)
        presenter = HistoryPresenterImpl(this, HistoryInteractorImpl())

        sortNew = orderSharedPreferences.isSortedNew(HISTORY_KEY)

        historyAdapter = HistoryAdapter(requireContext(), this)

        val isTabletLand = requireContext().resources.getBoolean(R.bool.isTabletLand)

        with(binding.historyRecycler) {
            layoutManager = if (isTabletLand) LinearLayoutManager(
                this@HistoryFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            ) else LinearLayoutManager(this@HistoryFragment.context)
            adapter = historyAdapter
            addItemDecoration(HeaderItemDecoration(this, historyAdapter.isHeader(), isTabletLand))
        }

        binding.swipeRefresh.setOnRefreshListener {
            apiState = ApiState.PENDING
            presenter?.get(sortNew)
        }

        presenter?.get(sortNew)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.sort_new -> {
                if (!sortNew) {
                    sortNew = true
                    orderSharedPreferences.setSortOrder(HISTORY_KEY, sortNew)
                    presenter?.get(sortNew)
                }
                true
            }
            R.id.sort_old -> {
                if (sortNew) {
                    sortNew = false
                    orderSharedPreferences.setSortOrder(HISTORY_KEY, sortNew)
                    presenter?.get(sortNew)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun update(response: ArrayList<HistoryHeaderModel>) {
        apiState = ApiState.SUCCESS

        historyAdapter.update(response)

        binding.historyRecycler.apply {
            smoothScrollToPosition(0)
            scheduleLayoutAnimation()
        }
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showProgress() {
        binding.toolbarLayout.progress.show()
    }

    override fun hideProgress() {
        binding.toolbarLayout.progress.hide()
    }

    override fun showError(error: String) {
        apiState = ApiState.FAILED
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.get(sortNew)
            ApiState.SUCCESS -> {
            }
        }
    }

}
