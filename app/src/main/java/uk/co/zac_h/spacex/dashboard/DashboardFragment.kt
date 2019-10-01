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
import uk.co.zac_h.spacex.dashboard.adapters.DashboardLaunchesAdapter
import uk.co.zac_h.spacex.dashboard.adapters.DashboardPinnedAdapter
import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class DashboardFragment : Fragment(), DashboardView {

    private lateinit var presenter: DashboardPresenter
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private lateinit var dashboardLaunchesAdapter: DashboardLaunchesAdapter
    private lateinit var pinnedAdapter: DashboardPinnedAdapter

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

        dashboard_swipe_refresh.setOnRefreshListener {
            presenter.getLatestLaunches(true)
        }

        presenter.getLatestLaunches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        presenter.cancelRequests()
    }

    override fun setLaunchesList(launches: LinkedHashMap<String, LaunchesModel>) {
        dashboardLaunchesAdapter = DashboardLaunchesAdapter(context, launches)

        dashboard_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            setHasFixedSize(true)
            adapter = dashboardLaunchesAdapter
        }
    }

    override fun setPinnedList(pinned: ArrayList<LaunchesModel>) {
        pinnedAdapter = DashboardPinnedAdapter(context, pinned)

        dashboard_pinned_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            setHasFixedSize(true)
            adapter = pinnedAdapter
        }
    }

    override fun updateLaunchesList() {
        dashboardLaunchesAdapter.notifyDataSetChanged()
    }

    override fun updatePinnedList() {
        pinnedAdapter.notifyDataSetChanged()
    }

    override fun setCountdown(launchDateUnix: Long) {
        val time = launchDateUnix.times(1000) - System.currentTimeMillis()

        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {

            }
        }.start()
    }

    override fun updateCountdown(countdown: String) {
        dashboard_countdown_text?.text = countdown
    }

    override fun showPinnedHeading() {
        dashboard_pinned_heading_text.visibility = View.VISIBLE
    }

    override fun hidePinnedHeading() {
        dashboard_pinned_heading_text.visibility = View.GONE
    }

    override fun showProgress() {
        dashboard_progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        dashboard_progress_bar.visibility = View.GONE
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        dashboard_swipe_refresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
