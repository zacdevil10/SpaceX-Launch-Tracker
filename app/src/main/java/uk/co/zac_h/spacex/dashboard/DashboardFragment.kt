package uk.co.zac_h.spacex.dashboard

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
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class DashboardFragment : Fragment(), DashboardView {

    private lateinit var presenter: DashboardPresenter

    private lateinit var dashboardLaunchesAdapter: DashboardLaunchesAdapter
    private var launchMap = LinkedHashMap<String, LaunchesModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardLaunchesAdapter = DashboardLaunchesAdapter(context, launchMap)

        presenter = DashboardPresenterImpl(this, DashboardInteractorImpl())

        dashboard_launches_recycler.apply {
            layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            setHasFixedSize(true)
            adapter = dashboardLaunchesAdapter
        }

        dashboard_swipe_refresh.setOnRefreshListener {
            presenter.apply {
                getSingleLaunch("next")
                getSingleLaunch("latest")
            }
        }

        presenter.apply {
            getSingleLaunch("next")
            getSingleLaunch("latest")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.cancelRequests()
    }

    override fun updateLaunchesList(id: String, launchesModel: LaunchesModel?) {
        if (launchesModel != null) {
            launchMap[id] = launchesModel
        }

        if (launchMap.size == 2) dashboardLaunchesAdapter.notifyDataSetChanged()
    }

    override fun toggleProgress(visibility: Int) {
        dashboard_progress_bar.visibility = visibility
    }

    override fun toggleSwipeProgress(isRefreshing: Boolean) {
        dashboard_swipe_refresh.isRefreshing = isRefreshing
    }

    override fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}
