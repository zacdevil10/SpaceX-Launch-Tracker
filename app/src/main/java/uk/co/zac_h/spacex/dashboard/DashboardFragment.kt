package uk.co.zac_h.spacex.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.dashboard.adapters.DashboardLaunchesAdapter
import uk.co.zac_h.spacex.utils.DashboardListModel
import uk.co.zac_h.spacex.utils.HeaderItemDecoration
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class DashboardFragment : Fragment(), DashboardView {

    private lateinit var presenter: DashboardPresenter
    private lateinit var pinnedSharedPreferences: PinnedSharedPreferencesHelper

    private lateinit var dashboardLaunchesAdapter: DashboardLaunchesAdapter

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

        dashboard_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            setHasFixedSize(true)
        }

        dashboard_swipe_refresh.setOnRefreshListener {
            presenter.getLatestLaunches()
        }

        presenter.getLatestLaunches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
        dashboard_launches_recycler.adapter = null
    }

    override fun updateLaunchesList(launches: ArrayList<ArrayList<DashboardListModel>>) {
        println("The launches list: $launches")
        dashboardLaunchesAdapter = DashboardLaunchesAdapter(context, launches)

        dashboard_launches_recycler.apply {
            adapter = dashboardLaunchesAdapter
            addItemDecoration(HeaderItemDecoration(this, dashboardLaunchesAdapter.isHeader()))
        }
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
