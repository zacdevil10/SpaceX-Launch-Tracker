package uk.co.zac_h.spacex.about.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.OrderSharedPreferencesHelperImpl
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener
import uk.co.zac_h.spacex.utils.views.HeaderItemDecoration

class HistoryFragment : Fragment(), HistoryView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

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
            it.getParcelableArrayList<HistoryHeaderModel>("history") as ArrayList<HistoryHeaderModel>
        } ?: ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        orderSharedPreferences = OrderSharedPreferencesHelperImpl.build(context)
        presenter = HistoryPresenterImpl(this, HistoryInteractorImpl())

        sortNew = orderSharedPreferences.isSortedNew("history")

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

        binding?.historySwipeRefresh?.setOnRefreshListener {
            presenter?.get(sortNew)
        }

        if (history.isEmpty()) {
            presenter?.get(sortNew)
        }
    }

    override fun onResume() {
        super.onResume()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList("history", history)
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
                    orderSharedPreferences.setSortOrder("history", sortNew)
                    presenter?.get(sortNew)
                }
                true
            }
            R.id.sort_old -> {
                if (sortNew) {
                    sortNew = false
                    orderSharedPreferences.setSortOrder("history", sortNew)
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
        binding?.progressIndicator?.show()
    }

    override fun hideProgress() {
        binding?.progressIndicator?.hide()
    }

    override fun toggleSwipeRefresh(isRefreshing: Boolean) {
        binding?.historySwipeRefresh?.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {

    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            binding?.let {
                if (history.isEmpty() || it.progressIndicator.isShown)
                    presenter?.get(sortNew)
            }
        }
    }

}
