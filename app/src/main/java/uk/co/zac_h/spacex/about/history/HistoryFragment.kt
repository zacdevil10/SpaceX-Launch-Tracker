package uk.co.zac_h.spacex.about.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

class HistoryFragment : BaseFragment(), HistoryView {

    companion object {
        const val HISTORY_KEY = "history"
    }

    override var title: String = "History"

    private var binding: FragmentHistoryBinding? = null

    private var presenter: NetworkInterface.Presenter<Nothing>? = null
    private lateinit var history: ArrayList<HistoryHeaderModel>

    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var orderSharedPreferences: OrderSharedPreferencesHelper
    private var sortNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        history = savedInstanceState?.let {
            it.getParcelableArrayList<HistoryHeaderModel>(HISTORY_KEY) as ArrayList<HistoryHeaderModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHistoryBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        orderSharedPreferences = OrderSharedPreferencesHelperImpl.build(context)
        presenter = HistoryPresenterImpl(this, HistoryInteractorImpl())

        sortNew = orderSharedPreferences.isSortedNew(HISTORY_KEY)

        historyAdapter = HistoryAdapter(requireContext(), history, this)

        val isTabletLand = context?.resources?.getBoolean(R.bool.isTabletLand)

        binding?.historyRecycler?.apply {
            layoutManager = isTabletLand?.let {
                if (isTabletLand) {
                    LinearLayoutManager(
                        this@HistoryFragment.context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                } else {
                    LinearLayoutManager(this@HistoryFragment.context)
                }
            } ?: LinearLayoutManager(this@HistoryFragment.context)
            setHasFixedSize(true)
            adapter = historyAdapter
            addItemDecoration(
                HeaderItemDecoration(
                    this,
                    historyAdapter.isHeader(),
                    isTabletLand ?: false
                )
            )
        }

        binding?.swipeRefresh?.setOnRefreshListener {
            presenter?.get(sortNew)
        }

        if (history.isEmpty()) {
            presenter?.get(sortNew)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(HISTORY_KEY, history)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.cancelRequest()
        binding = null
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
        history.clear()
        history.addAll(response)

        historyAdapter.notifyDataSetChanged()

        binding?.historyRecycler?.apply {
            smoothScrollToPosition(0)
            scheduleLayoutAnimation()
        }
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showProgress() {
        binding?.progress?.show()
    }

    override fun hideProgress() {
        binding?.progress?.hide()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.swipeRefresh?.isRefreshing = isRefreshing
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (history.isEmpty() || it.progress.isShown)
                    presenter?.get(sortNew)
            }
        }
    }

}
