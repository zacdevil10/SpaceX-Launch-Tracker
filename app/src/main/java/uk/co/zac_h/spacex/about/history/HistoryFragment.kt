package uk.co.zac_h.spacex.about.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.utils.ApiState
import uk.co.zac_h.spacex.utils.Keys
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

class HistoryFragment : BaseFragment(), HistoryContract.View {

    override val title: String by lazy { getString(R.string.menu_history) }

    private lateinit var binding: FragmentHistoryBinding

    private var presenter: HistoryContract.Presenter? = null

    private lateinit var historyAdapter: HistoryAdapter
    private var history: ArrayList<HistoryHeaderModel>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        history = savedInstanceState?.getParcelableArrayList(Keys.HISTORY.HISTORY_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHistoryBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarLayout.toolbar.apply {
            setSupportActionBar()
            setup()
        }

        presenter = HistoryPresenterImpl(
            this,
            HistoryInteractorImpl(),
            OrderSharedPreferencesHelperImpl.build(requireContext())
        )

        historyAdapter = HistoryAdapter(requireContext(), this)

        val isTabletLand = resources.getBoolean(R.bool.isTabletLand)

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
            presenter?.get()
        }

        presenter?.getOrUpdate(history)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(Keys.HISTORY.HISTORY_KEY, history)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.sort_new -> handleSortItemClick(false)
            R.id.sort_old -> handleSortItemClick(true)
            else -> super.onOptionsItemSelected(item)
        }

    private fun handleSortItemClick(order: Boolean): Boolean {
        history = null
        if (presenter?.getOrder() == order) presenter?.apply {
            setOrder(!order)
            get()
        }
        return true
    }

    override fun update(response: ArrayList<HistoryHeaderModel>) {
        apiState = ApiState.SUCCESS

        history = response
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
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding.swipeRefresh.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        when (apiState) {
            ApiState.PENDING, ApiState.FAILED -> presenter?.get()
            ApiState.SUCCESS -> Log.i(title, "Network available and data loaded")
        }
    }

}
