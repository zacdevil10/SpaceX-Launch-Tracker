package uk.co.zac_h.spacex.about.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.about.adapter.HistoryAdapter
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentHistoryBinding
import uk.co.zac_h.spacex.utils.HeaderItemDecoration
import uk.co.zac_h.spacex.utils.models.HistoryHeaderModel
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class HistoryFragment : Fragment(), HistoryContract.HistoryView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private var presenter: HistoryContract.HistoryPresenter? = null

    private lateinit var history: ArrayList<HistoryHeaderModel>
    private lateinit var historyAdapter: HistoryAdapter

    private var sortNew = true

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
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setDrawerLayout(drawerLayout).build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        presenter = HistoryPresenterImpl(this, HistoryInteractorImpl())

        historyAdapter = HistoryAdapter(requireContext(), history, this)

        val isTabletLand = context?.resources?.getBoolean(R.bool.isTabletLand)

        binding.historyRecycler.apply {
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

        binding.historySwipeRefresh.setOnRefreshListener {
            presenter?.getHistory(sortNew)
        }

        if (history.isEmpty()) {
            presenter?.getHistory(sortNew)
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
                    presenter?.getHistory(sortNew)
                }
                true
            }
            R.id.sort_old -> {
                if (sortNew) {
                    sortNew = false
                    presenter?.getHistory(sortNew)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun addHistory(history: ArrayList<HistoryHeaderModel>) {
        this.history.clear()
        this.history.addAll(history)

        historyAdapter.notifyDataSetChanged()

        binding.historyRecycler.scheduleLayoutAnimation()
    }

    override fun openWebLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun showProgress() {
        binding.historyProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.historyProgressBar.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        binding.historySwipeRefresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            if (history.isEmpty() || binding.historyProgressBar.visibility == View.VISIBLE)
                presenter?.getHistory(sortNew)
        }
    }

}
