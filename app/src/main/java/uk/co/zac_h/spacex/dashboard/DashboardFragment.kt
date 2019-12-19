package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.dashboard.adapters.DashboardLaunchesAdapter
import uk.co.zac_h.spacex.dashboard.adapters.DashboardPinnedAdapter
import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class DashboardFragment : Fragment(), DashboardView,
    OnNetworkStateChangeListener.NetworkStateReceiverListener {

    private var presenter: DashboardPresenter? = null
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private lateinit var dashboardLaunchesAdapter: DashboardLaunchesAdapter
    private lateinit var pinnedAdapter: DashboardPinnedAdapter

    private val pinnedArray = ArrayList<LaunchesModel>()

    private var countdownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinnedSharedPreferences = PinnedSharedPreferencesHelper(
            context?.getSharedPreferences(
                "pinned",
                Context.MODE_PRIVATE
            )
        )

        presenter = DashboardPresenterImpl(this, pinnedSharedPreferences, DashboardInteractorImpl())

        pinnedAdapter = DashboardPinnedAdapter(context, pinnedArray)

        dashboard_pinned_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            setHasFixedSize(true)
            adapter = pinnedAdapter
        }

        dashboard_swipe_refresh.setOnRefreshListener {
            presenter?.getLatestLaunches()
        }

        presenter?.getLatestLaunches()
    }

    override fun onStart() {
        super.onStart()
        (context?.applicationContext as App).networkStateChangeListener.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        pinnedArray.clear()
        dashboard_swipe_refresh.isEnabled = false
    }

    override fun onStop() {
        super.onStop()
        (context?.applicationContext as App).networkStateChangeListener.removeListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        presenter?.cancelRequests()
    }

    override fun setLaunchesList(launches: LinkedHashMap<String, LaunchesModel>) {
        dashboardLaunchesAdapter = DashboardLaunchesAdapter(context, launches)

        dashboard_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            setHasFixedSize(true)
            adapter = dashboardLaunchesAdapter
        }
    }

    override fun updateLaunchesList() {
        dashboardLaunchesAdapter.notifyDataSetChanged()
    }

    override fun updatePinnedList(pinned: LinkedHashMap<String, LaunchesModel>) {
        pinnedArray.clear()
        pinnedArray.addAll(pinned.values)

        pinnedAdapter.notifyDataSetChanged()
    }

    override fun setCountdown(launchDateUnix: Long) {
        val time = launchDateUnix.times(1000) - System.currentTimeMillis()

        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter?.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {

            }
        }

        countdownTimer?.start()
    }

    override fun updateCountdown(countdown: String) {
        dashboard_countdown_text?.text = countdown
    }

    override fun showPinnedMessage() {
        if (pinnedArray.isEmpty()) dashboard_pinned_message_text.visibility = View.VISIBLE
    }

    override fun hidePinnedMessage() {
        dashboard_pinned_message_text.visibility = View.GONE
    }

    override fun showProgress() {
        dashboard_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        dashboard_progress_bar.visibility = View.GONE
    }

    override fun showCountdown() {
        dashboard_countdown_text.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        dashboard_countdown_text.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        dashboard_swipe_refresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun networkAvailable() {
        activity?.runOnUiThread {
            presenter?.getLatestLaunches()
        }
    }
}
